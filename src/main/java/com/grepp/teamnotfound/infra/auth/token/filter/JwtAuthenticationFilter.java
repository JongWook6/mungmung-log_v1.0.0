package com.grepp.teamnotfound.infra.auth.token.filter;

import com.grepp.teamnotfound.app.model.auth.token.dto.AccessTokenDto;
import com.grepp.teamnotfound.app.model.auth.token.RefreshTokenService;
import com.grepp.teamnotfound.app.model.auth.token.entity.RefreshToken;
import com.grepp.teamnotfound.app.model.auth.token.repository.UserBlackListRepository;
import com.grepp.teamnotfound.infra.auth.token.JwtProvider;
import com.grepp.teamnotfound.infra.auth.token.TokenCookieFactory;
import com.grepp.teamnotfound.infra.auth.token.code.TokenType;
import com.grepp.teamnotfound.infra.error.exception.CommonException;
import com.grepp.teamnotfound.infra.error.exception.code.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserBlackListRepository userBlackListRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<String> excludePath = new ArrayList<>();
        excludePath.addAll(List.of("/error", "/favicon.ico", "/img","/download"));

        // TODO 관리자 회원가입/로그인 구현 시 추가
        excludePath.addAll(List.of("/api/v1/auth/register", "/api/v1/auth/login"));
        String path = request.getRequestURI();
        return excludePath.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getRequestURI());

        // 1. accessToken 확인 - 토큰이 없는 경우 filtering
        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. parseClaim - // 블랙리스트 filtering
        Claims claims = jwtProvider.parseClaims(accessToken);
        if(userBlackListRepository.existsById(claims.getSubject())){
            filterChain.doFilter(request, response);
            return;
        }


        // 3. 유효성 검증 validateToken
        try {
            if(jwtProvider.validateToken(accessToken)) {
                Authentication authentication = jwtProvider.generateAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // 3-1. 만료된 토큰 -> 재발급 - 재발급 실패 시 filtering
        } catch (ExpiredJwtException e) {
            AccessTokenDto newAccessToken = renewingAccessToken(accessToken, request);
            if(newAccessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }
            // 3-2. refreshToken 갱신
            RefreshToken newRefreshToken = renewingRefreshToken(claims.getId(), newAccessToken.getId());
            // 갱신된 Token으로 응답 생성
            responseToken(response, newAccessToken, newRefreshToken);
        }

        filterChain.doFilter(request, response);
    }



    // AccessToken 만료 시 재발급
    private AccessTokenDto renewingAccessToken(String accessToken, HttpServletRequest request) {
        // 만료된 accessToken으로 인증 객체 생성?
        Authentication authentication = jwtProvider.generateAuthentication(accessToken);
        String refreshToken = jwtProvider.resolveToken(request, TokenType.REFRESH_TOKEN);
        Claims claims = jwtProvider.parseClaims(accessToken);

        // refresh 활성화(?) 여부 확인
        RefreshToken storedRefreshToken = refreshTokenService.findByAccessTokenId(claims.getId());
        if(storedRefreshToken == null) {
            return null;
        }

        //TODO 블랙리스트 추가
        if(!storedRefreshToken.getToken().equals(refreshToken)){
            throw new CommonException(AuthErrorCode.INVALID_TOKEN);
        }

        return generateAccessToken(authentication);
    }

    private AccessTokenDto generateAccessToken(Authentication authentication) {
        AccessTokenDto newAccessToken = jwtProvider.generateAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return newAccessToken;
    }

    // accessToken 재발급 시, refreshToken 재발급 로직
    private RefreshToken renewingRefreshToken(String id, String newTokenId) {
        return refreshTokenService.renewingToken(id, newTokenId);
    }

    // 응답에 쿠키로 토큰 전달
    private void responseToken(HttpServletResponse response, AccessTokenDto newAccessToken, RefreshToken newRefreshToken) {
        ResponseCookie accessTokenCookie =
                // TODO test용 브라우저 길이 : 추후 수정 필수
                // jwtProvider.getAtExpiration()
                TokenCookieFactory.create(TokenType.ACCESS_TOKEN.name(), newAccessToken.getToken(),
                        3000000L);

        ResponseCookie refreshTokenCookie =
                TokenCookieFactory.create(TokenType.REFRESH_TOKEN.name(), newRefreshToken.getToken(),
                        jwtProvider.getRtExpiration());

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
