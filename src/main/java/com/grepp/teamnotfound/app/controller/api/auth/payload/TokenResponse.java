package com.grepp.teamnotfound.app.controller.api.auth.payload;

import com.grepp.teamnotfound.app.model.auth.dto.LoginResult;
import com.grepp.teamnotfound.infra.auth.token.code.GrantType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private GrantType grantType;
    private Long expiresIn;

    public static TokenResponse of(LoginResult result){
        return TokenResponse.builder()
                .accessToken(result.getAccessToken())
                .refreshToken(result.getRefreshToken())
                .grantType(GrantType.BEARER)
                .expiresIn(result.getAtExpiresIn())
                .build();
    }
}
