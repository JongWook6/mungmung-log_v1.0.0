package com.grepp.teamnotfound.app.model.user.dto;

import com.grepp.teamnotfound.app.controller.api.auth.payload.RegisterRequest;
import lombok.*;

@Getter
@Builder
@RequiredArgsConstructor
public class RegisterCommand {

    private final String email;
    private final String name;
    private final String nickname;
    private final String password;

    public static RegisterCommand of(RegisterRequest request){
        return RegisterCommand.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .build();
    }
}
