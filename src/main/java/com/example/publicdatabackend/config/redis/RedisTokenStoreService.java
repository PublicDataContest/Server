package com.example.publicdatabackend.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//레디스에 토큰을 저장하기 위한 클래스
@Service
public class RedisTokenStoreService {
    private static final Logger logger = LoggerFactory.getLogger(RedisTokenStoreService.class);

    @Value("${jwt.refreshTokenExpiration}")
    private int jwtRefreshTokenExpirationInMs;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //레디스에 저장
    //리프레시 토큰인 경우에는 사용자 id(pk값)과 함께 저장
    //액세스 토큰인 경우에는 사용자 id(pk값)만 저장
    public void storeToken(String token, String userId, boolean isRefreshToken) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        if (isRefreshToken) {
            values.set(token, userId, jwtRefreshTokenExpirationInMs, TimeUnit.MILLISECONDS);
            logger.info("Refresh token stored for user {}: {}", userId, token);
        } else {
            values.set(token, userId, jwtRefreshTokenExpirationInMs, TimeUnit.MILLISECONDS);
            logger.info("Access token stored for user {}: {}", userId, token);
        }
    }

    //해당 토큰과 연결된 사용자 이름을 레디스에서 검색 & 반환하는 메서드
    public String retrieveUserName(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String userId = values.get(token);
        logger.info("Retrieved user for token {}: {}", token, userId);
        return userId;
    }

    //토큰을 레디스에서 삭제하는 메서드
    public void removeToken(String token) {
        redisTemplate.delete(token);
        logger.info("Token deleted: {}", token);
    }
}
