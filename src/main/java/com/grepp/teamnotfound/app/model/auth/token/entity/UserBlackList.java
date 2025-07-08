package com.grepp.teamnotfound.app.model.auth.token.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.OffsetDateTime;

@Getter
@Setter
@RedisHash("userBlackList")
public class UserBlackList {

    @Id
    private String email;
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @TimeToLive
    private Long expiration;

    // 이메일과 TTL을 받는 생성자
    public UserBlackList(String email, Long expiration) {
        this.email = email;
        this.expiration = expiration;
    }

}
