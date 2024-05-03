package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.config.security.CustomUserDetailsService;
import com.example.publicdatabackend.dto.login.LoginRequest;
import com.example.publicdatabackend.dto.login.LoginResponse;
import com.example.publicdatabackend.dto.register.RegisterRequest;
import com.example.publicdatabackend.dto.register.RegisterResponse;
import com.example.publicdatabackend.dto.token.RefreshTokenRequest;
import com.example.publicdatabackend.dto.token.RefreshTokenResponse;
import com.example.publicdatabackend.service.AuthService;
import com.example.publicdatabackend.global.res.BaseResponse;
import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import com.example.publicdatabackend.global.res.constant.StatusCodeConstant;
import com.example.publicdatabackend.config.jwt.JwtTokenProvider;
import com.example.publicdatabackend.config.security.UserPrincipal;
import com.example.publicdatabackend.config.redis.RedisTokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final RedisTokenStoreService tokenStoreService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

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

    @GetMapping("/check/username")
    public ResponseEntity<BaseResponse> checkUsernameAvailability(@RequestParam String username) {
        boolean isTaken = authService.isUsernameTaken(username);

        if (isTaken) {
            // 아이디가 이미 사용 중인 경우
            BaseResponse response = new BaseResponse(StatusCodeConstant.BAD_REQUEST_STATUS_CODE, "이미 사용중인 아이디입니다.");
            return ResponseEntity.ok(response);
        } else {
            // 아이디 사용 가능한 경우
            BaseResponse response = new BaseResponse(StatusCodeConstant.OK_STATUS_CODE, "사용가능한 아이디입니다.");
            return ResponseEntity.ok(response);
        }
    }


    @PostMapping("/api/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            String username = tokenStoreService.retrieveUserName(refreshToken);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
            }

            UserPrincipal userDetails = (UserPrincipal) customUserDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            tokenStoreService.removeToken(refreshToken);
            tokenStoreService.storeToken(newRefreshToken, username, true);

            return ResponseEntity.ok(new RefreshTokenResponse(userDetails.getId(), newAccessToken, newRefreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or Expired Refresh Token");
        }
    }

}
