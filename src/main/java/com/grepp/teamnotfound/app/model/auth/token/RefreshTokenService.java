package com.grepp.teamnotfound.app.model.auth.token;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.teamnotfound.app.model.auth.token.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;


    public RefreshToken saveWithAtId(String atId){
        RefreshToken refreshToken = new RefreshToken(atId);
        redisTemplate.opsForValue().set(atId, refreshToken, Duration.ofSeconds(refreshToken.getTtl()));
        return refreshToken;
    }

    public RefreshToken findByAccessTokenId(String atId) {
        Object map = redisTemplate.opsForValue().get(atId);
        return objectMapper.convertValue(map, RefreshToken.class);
    }

    // 신규 refreshToken 발급 로직
    public RefreshToken renewingToken(String oldTokenId, String newTokenId) {
        RefreshToken refreshToken = findByAccessTokenId(oldTokenId);

        if(refreshToken == null) return null;

        // 지연시간 동안 사용할 ttl 10초 짜리
        RefreshToken gracePeriodToken = new RefreshToken(oldTokenId);
        gracePeriodToken.setToken(refreshToken.getToken());

        // 기존 refresh token 변경
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setAtId(newTokenId);

        redisTemplate.opsForValue().set(newTokenId, refreshToken, Duration.ofSeconds(refreshToken.getTtl()));
        redisTemplate.opsForValue().set(oldTokenId, gracePeriodToken, Duration.ofSeconds(1000));
        return refreshToken;
    }



    public void deleteByAccessTokenId(String atId) {
        redisTemplate.delete(atId);
    }


}
