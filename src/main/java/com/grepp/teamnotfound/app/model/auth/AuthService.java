package com.grepp.teamnotfound.app.model.auth;

import com.google.api.JwtLocationOrBuilder;
import com.grepp.teamnotfound.app.model.auth.payload.LoginRequest;
import com.grepp.teamnotfound.app.model.auth.token.RefreshTokenService;
import com.grepp.teamnotfound.app.model.auth.token.dto.AccessTokenDto;
import com.grepp.teamnotfound.app.model.auth.token.dto.TokenDto;
import com.grepp.teamnotfound.app.model.auth.token.entity.RefreshToken;
import com.grepp.teamnotfound.infra.auth.token.JwtProvider;
import com.grepp.teamnotfound.infra.auth.token.code.GrantType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;


    public TokenDto login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return processTokenLogin(authentication.getName());
    }

    private TokenDto processTokenLogin(String email) {

        // TODO BlackList
//        userBlackListRepository.

        AccessTokenDto accessToken = jwtProvider.generateAccessToken(email);
        RefreshToken refreshToken = refreshTokenService.saveWithAtId(accessToken.getId());

        return TokenDto.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .grantType(GrantType.BEARER)
                .atExpiresIn(jwtProvider.getAtExpiration())
                .rtExpiresIn(jwtProvider.getRtExpiration())
                .build();
    }
}
