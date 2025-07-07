package com.grepp.teamnotfound.infra.auth.token;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.auth.dto.TokenResponseDto;
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
import java.util.UUID;

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

    // 우선 access/refresh 한 번에 제작
    public TokenResponseDto createToken(Long userId, Role role){
        Date now = new Date();

        // access
        String accessToken = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + atExpiration * 1000))
                .signWith(key)
                .compact();

        // 3-2. Refresh Token 생성 (지금은 특별한 정보 없이 만료 시간만 설정)
        String refreshToken = Jwts.builder()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + rtExpiration * 1000))
                .signWith(key)
                .compact();

        // 3-3. DTO에 담아 반환
        return TokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


//    public AccessTokenDto generateAccessToken(Authentication authentication){
//        return generateAccessToken(authentication.getName());
//    }
//
//    public AccessTokenDto generateAccessToken(String username){
//        String id = UUID.randomUUID().toString();
//        long now = new Date().getTime();
//        Date atExpiresIn = new Date(now + atExpiration);
//        String accessToken = Jwts.builder()
//                .subject(username)
//                .id(id)
//                .expiration(atExpiresIn)
//                .signWith(getSecretKey())
//                .compact();
//
//        return AccessTokenDto.builder()
//                .id(id)
//                .token(accessToken)
//                .expiresIn(atExpiration)
//                .build();
//    }

//    public Authentication genreateAuthentication(String accessToken){
//        Claims claims = parseClaim(accessToken);
//        List<? extends GrantedAuthority> authorities = userDetailsService.findAuthorities(claims.getSubject());
//        Principal principal = new Principal(claims.getSubject(),"", authorities);
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }
//
//    public Claims parseClaim(String accessToken) {
//        try{
//            return Jwts.parser().verifyWith(getSecretKey()).build()
//                    .parseSignedClaims(accessToken).getPayload();
//        }catch (ExpiredJwtException ex){
//            return ex.getClaims();
//        }
//    }
//
//    public boolean validateToken(String requestAccessToken) {
//        try{
//            Jwts.parser().verifyWith(getSecretKey()).build().parse(requestAccessToken);
//            return true;
//        }catch(SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){
//            log.error(e.getMessage(), e);
//        }
//        return false;
//    }
//
//    public String resolveToken(HttpServletRequest request, TokenType tokenType) {
//        String headerToken = request.getHeader("Authorization");
//        if (headerToken != null && headerToken.startsWith("Bearer")) {
//            return headerToken.substring(7);
//        }
//
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) {
//            return null;
//        }
//
//        return Arrays.stream(cookies)
//                .filter(e -> e.getName().equals(tokenType.name()))
//                .map(Cookie::getValue).findFirst()
//                .orElse(null);
//    }

}
