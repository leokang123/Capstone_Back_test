package com.example.mobileapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private final Long expireMs = 1000L * 60 * 60 * 24 * 14;
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set("RT:" + userId, refreshToken, expireMs, TimeUnit.MILLISECONDS);
    }

    public Boolean isValid(String userId, String refreshToken) {
        return Objects.equals(redisTemplate.opsForValue().get("RT:" + userId), refreshToken);
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("RT:" + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("RT:" + userId);
    }
}