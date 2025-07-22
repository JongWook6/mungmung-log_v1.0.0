package com.grepp.teamnotfound.app.controller.api.mypage.payload;

import lombok.Data;

@Data
public class UserWriteRequest {
    private String nickname;
    private String password;
}
