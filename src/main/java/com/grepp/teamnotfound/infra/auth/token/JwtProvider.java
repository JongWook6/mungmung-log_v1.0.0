package com.grepp.teamnotfound.infra.auth.token;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.auth.domain.Principal;
import com.grepp.teamnotfound.infra.auth.UserDetailsServiceImpl;
import com.grepp.teamnotfound.infra.auth.token.code.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    private final UserDetailsServiceImpl userDetailsService;
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

    // 3. token 검증 (만료 검증은 Exception 만 터트려서 filter에서 재발급 받도록 함)
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parse(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new JwtException("유효하지 않은 토큰입니다");
            // TODO Authen Filter에서 잡기
        } catch (ExpiredJwtException e) {
            throw e;
        }
    }


    // 4. 토큰 내 정보 추출
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 5. Authentication 제작
    public Authentication generateAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        List<? extends GrantedAuthority> authorities = userDetailsService.findAuthorities(claims.getSubject());

        Principal principal = new Principal(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    // 6. 토큰 추출 resolver
    public String resolveToken(HttpServletRequest request, TokenType tokenType) {
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null && headerToken.startsWith("Bearer")) {
            return headerToken.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(e -> e.getName().equals(tokenType.name()))
                .map(Cookie::getValue).findFirst()
                .orElse(null);
    }
}
