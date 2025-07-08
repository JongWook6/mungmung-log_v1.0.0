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

//    public UserBlackList(String email) {
//        this.email = email;
//    }

    @TimeToLive // 이 필드의 Long 값이 Redis의 TTL(초 단위)로 설정됩니다.
    private Long expiration; // ✅ 이 필드를 추가합니다.

    // 이메일과 TTL을 받는 생성자
    public UserBlackList(String email, Long expiration) {
        this.email = email;
        this.expiration = expiration;
    }

}
