package com.grepp.teamnotfound.app.model.user.dto;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.user.entity.User;
import lombok.Data;

@Data
public class UserDto {

    private Long userId;
    private String email;
    private Boolean state;
    private String name;
    private String nickname;
    private Role role;
    private String provider;
    private String imgUrl;

    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setState(user.getState());
        dto.setName(user.getName());
        dto.setNickname(user.getNickname());
        dto.setRole(user.getRole());
        dto.setProvider(user.getProvider());

        return dto;
    }
}
