package com.grepp.teamnotfound.app.controller.api.auth.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Long userId;
    private TokenResponse tokenResponse;
}
