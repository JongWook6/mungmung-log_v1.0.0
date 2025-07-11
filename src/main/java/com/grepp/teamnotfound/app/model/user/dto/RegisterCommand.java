package com.grepp.teamnotfound.app.model.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class RegisterCommand {

    private String email;
    private String name;
    private String nickname;
    private String password;
}
