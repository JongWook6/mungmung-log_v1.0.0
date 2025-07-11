package com.grepp.teamnotfound.app.model.user.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCommand {

    private String email;
    private String name;
    private String nickname;
    private String password;
}
