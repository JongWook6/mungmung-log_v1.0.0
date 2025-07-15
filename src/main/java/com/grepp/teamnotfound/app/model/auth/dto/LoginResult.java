package com.grepp.teamnotfound.app.model.auth.dto;

import com.grepp.teamnotfound.infra.auth.token.code.GrantType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResult {
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private GrantType grantType;
    private Long atExpiresIn;
    private Long rtExpiresIn;
}
