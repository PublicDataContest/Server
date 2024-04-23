package com.example.publicdatabackend.auth.controller;

import com.example.publicdatabackend.auth.dto.LoginRequest;
import com.example.publicdatabackend.auth.dto.LoginResponse;
import com.example.publicdatabackend.auth.dto.RegisterRequest;
import com.example.publicdatabackend.auth.dto.RegisterResponse;
import com.example.publicdatabackend.auth.service.AuthService;
import com.example.publicdatabackend.global.res.BaseResponse;
import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import com.example.publicdatabackend.global.res.constant.StatusCodeConstant;
import com.example.publicdatabackend.security.jwt.JwtTokenProvider;
import com.example.publicdatabackend.security.jwt.UserPrincipal;
import com.example.publicdatabackend.security.service.RedisTokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final RedisTokenStoreService tokenStoreService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            LoginResponse loginResponse = authService.localLogin(loginRequest);
            String accessToken = loginResponse.getAccessToken();
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            // 토큰을 레디스에 저장
            tokenStoreService.storeToken(accessToken, loginRequest.getUserName(), true);
            tokenStoreService.storeToken(refreshToken, loginRequest.getUserName(), true);

            return ResponseEntity.ok(new LoginResponse(userPrincipal.getId(), accessToken, refreshToken));
        } catch (AuthenticationException e) {
            // 인증 실패: 비밀번호가 일치하지 않음
            if (authService.isUsernameTaken(loginRequest.getUserName())) {
                BaseResponse response = new BaseResponse(StatusCodeConstant.BAD_REQUEST_STATUS_CODE, ResponseMessageConstant.LOGIN_FAIL);
                return ResponseEntity.badRequest().body(response);
            } else {
                BaseResponse response = new BaseResponse(StatusCodeConstant.BAD_REQUEST_STATUS_CODE, ResponseMessageConstant.USER_NOT_FOUND);
                return ResponseEntity.badRequest().body(response);
            }
        }
    }


    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest registerRequest) {
        // 중복된 아이디인지 확인
        if (authService.isUsernameTaken(registerRequest.getUserName())) {
            // 중복된 아이디일 경우 400 Bad Request 반환
            BaseResponse response = new BaseResponse(StatusCodeConstant.BAD_REQUEST_STATUS_CODE, ResponseMessageConstant.REGISTER_FAIL);
            return ResponseEntity.badRequest().body(response);
        }

        // 중복되지 않은 경우 회원가입 처리
        RegisterResponse response = authService.register(registerRequest);

        // 회원가입 성공 응답
        BaseResponse baseResponse = new BaseResponse(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.REGISTER_SUCCESS);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }
}
