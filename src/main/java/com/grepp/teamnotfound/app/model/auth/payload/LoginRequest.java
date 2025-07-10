package com.grepp.teamnotfound.app.model.auth.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
