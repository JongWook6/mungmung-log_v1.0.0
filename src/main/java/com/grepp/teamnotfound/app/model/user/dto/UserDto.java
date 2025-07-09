package com.grepp.teamnotfound.app.model.user.dto;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import lombok.Data;

@Data
public class UserDto {

    private Long userId;

    private String email;

    private Boolean state;

    private String name;

    private String nickname;

    private Role role;

    private String password;

    private String provider;

    private UserImgDto userImg;

}
