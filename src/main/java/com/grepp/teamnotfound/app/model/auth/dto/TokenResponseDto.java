package com.grepp.teamnotfound.app.model.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
