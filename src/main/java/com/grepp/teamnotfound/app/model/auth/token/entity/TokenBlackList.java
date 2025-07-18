package com.grepp.teamnotfound.app.model.auth.token.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.OffsetDateTime;

@Getter
@Setter
@RedisHash("tokenBlackList")
public class TokenBlackList {

    @Id
    private String accessToken;
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @TimeToLive
    private Long expiration;

    // 이메일과 TTL을 받는 생성자
    public TokenBlackList(String accessToken, Long expiration) {
        this.accessToken = accessToken;
        this.expiration = expiration;
    }

}
