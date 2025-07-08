package com.grepp.teamnotfound.app.model.auth.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessTokenDto {

    private String id;
    private String token;
    private Long expiresIn;
}
