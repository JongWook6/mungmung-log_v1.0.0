package com.grepp.teamnotfound.app.controller.api.admin.payload;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UsersListRequest {

    @Min(value = 1, message="페이지는 1이상이어야 합니다.")
    private int page=1;
    @Min(value = 1, message="페이지는 1이상이어야 합니다.")
    private int size=5;

    private String email;
    private String nickname;

    private String sort = "asc";
    private String status;
}
