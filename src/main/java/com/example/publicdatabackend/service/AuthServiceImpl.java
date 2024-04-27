package com.example.publicdatabackend.service;

import com.example.publicdatabackend.dto.login.LoginRequest;
import com.example.publicdatabackend.dto.login.LoginResponse;
import com.example.publicdatabackend.dto.register.RegisterRequest;
import com.example.publicdatabackend.dto.register.RegisterResponse;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.repository.UserRepository;
import com.example.publicdatabackend.config.jwt.JwtTokenProvider;
import com.example.publicdatabackend.config.redis.RedisTokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenStoreService tokenStoreService;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        // 요청으로부터 받은 사용자 이름과 비밀번호를 이용하여 회원 생성
        Users newMember = Users.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // 회원 저장
        Users savedMember = memberRepository.save(newMember);

        // 회원 생성 시간 저장
        LocalDateTime createdTime = savedMember.getCreatedAt();

        // 생성된 회원 정보 반환
        return new RegisterResponse(createdTime);
    }

    //중복된 아이디인지 확인
    @Override
    public boolean isUsernameTaken(String username) {
        Users existingMember = memberRepository.findByUserName(username);
        return existingMember != null;
    }

    //비밀번호 틀렸을 때
    @Override
    public boolean isPasswordCorrect(String username, String password) {
        Users user = memberRepository.findByUserName(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }


    @Override
    public LoginResponse localLogin(LoginRequest request) {
        // 사용자 이름으로 회원 조회
        Users user = memberRepository.findByUserName(request.getUserName());
        if (user == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        // 비밀번호 일치 여부 확인
        if (!isPasswordCorrect(request.getUserName(), request.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 사용자 인증
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // AccessToken 생성
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        // RefreshToken 생성
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // RefreshToken 저장
        tokenStoreService.storeToken(refreshToken, user.getUserName(), true);

        // AccessToken 반환
        return new LoginResponse(user.getId(), accessToken, refreshToken);
    }
}
