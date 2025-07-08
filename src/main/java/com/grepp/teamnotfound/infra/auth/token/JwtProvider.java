package com.grepp.teamnotfound.infra.auth.token;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-expiration}")
    private long atExpiration;

    @Getter
    @Value("${jwt.refresh-expiration}")
    private long rtExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // 1. accessToken 생성
    public String generateAccessToken(Long userId, Role role) {
        Date now = new Date();

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + atExpiration * 1000))
                .signWith(key)
                .compact();
    }

    // 2. refreshToken 생성  // TODO (지금은 특별한 정보 없이 만료 시간만 설정)
    public String generateRefreshToken(Long userId, Role role) {
        Date now = new Date();

        return Jwts.builder()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + rtExpiration * 1000))
                .signWith(key)
                .compact();
    }

    // 3. token 검증

    // 4. 토큰 내 정보 추출

    // 5. Authentication 제작

    // 6. 토큰 추출 resolver

}
