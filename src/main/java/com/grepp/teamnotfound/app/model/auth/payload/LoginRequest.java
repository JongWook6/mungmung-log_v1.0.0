package com.grepp.teamnotfound.app.model.auth.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 4, max = 10)
    private String password;
}
