package com.grepp.teamnotfound.app.model.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    private String email;
    private String name;
    private String nickname;
    private String password;
}
