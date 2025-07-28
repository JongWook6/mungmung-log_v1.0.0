package com.grepp.teamnotfound.app.model.auth.dto;

import com.grepp.teamnotfound.app.controller.api.auth.payload.LoginRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginCommand {

    private String email;
    private String password;

    public static LoginCommand of(LoginRequest request){
        return LoginCommand.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

}
