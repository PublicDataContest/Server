package com.example.publicdatabackend.config.jwt;

import com.example.publicdatabackend.config.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

//JWT 토큰을 생성하고 유효성을 확인하는 클래스
@Service
@Slf4j
public class JwtTokenProvider {
    private Key key; // 클래스 변수로 Key 인스턴스를 추가

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenExpiration}")
    private int jwtAccessTokenExpirationInMs;

    @Value("${jwt.refreshTokenExpiration}")
    private int jwtRefreshTokenExpirationInMs;

    @PostConstruct
    public void init() {
        // Base64로 인코딩된 시크릿을 디코딩하여 Key 인스턴스 생성
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtAccessTokenExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshTokenExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token.", e);
            return false;
        }
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
}
