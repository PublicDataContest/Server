package com.example.publicdatabackend.auth.service;

import com.example.publicdatabackend.auth.dto.LoginRequest;
import com.example.publicdatabackend.auth.dto.LoginResponse;
import com.example.publicdatabackend.auth.dto.RegisterRequest;
import com.example.publicdatabackend.auth.dto.RegisterResponse;
import com.example.publicdatabackend.member.domain.Member;
import com.example.publicdatabackend.member.repository.UserRepository;
import com.example.publicdatabackend.security.jwt.JwtTokenProvider;
import com.example.publicdatabackend.security.service.RedisTokenStoreService;
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
        Member newMember = Member.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // 회원 저장
        Member savedMember = memberRepository.save(newMember);

        // 회원 생성 시간 저장
        LocalDateTime createdTime = savedMember.getCreatedAt();

        // 생성된 회원 정보 반환
        return new RegisterResponse(createdTime);
    }

    //중복된 아이디인지 확인
    @Override
    public boolean isUsernameTaken(String username) {
        Member existingMember = memberRepository.findByUserName(username);
        return existingMember != null;
    }


    @Override
    public LoginResponse localLogin(LoginRequest request) {
        // 사용자 이름으로 회원 조회
        Member user = memberRepository.findByUserName(request.getUserName());
        if (user == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
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
