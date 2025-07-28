package com.grepp.teamnotfound.app.controller.api.auth.payload;

import com.grepp.teamnotfound.app.model.auth.dto.LoginResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Long userId;
    private TokenResponse tokenResponse;

    public static LoginResponse of(TokenResponse tokenResponse, LoginResult dto) {
        return LoginResponse.builder()
                .tokenResponse(tokenResponse)
                .userId(dto.getUserId())
                .build();
    }
}
