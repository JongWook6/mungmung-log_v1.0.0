package com.grepp.teamnotfound.app.controller.api.admin.payload;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class UsersListResponse {

    private Long userId;
    private String email;
    private String nickname;
    private int postCount;
    private int commentCount;
    private OffsetDateTime lastLoginDate;
    private OffsetDateTime joinDate;
    private String status;
    private OffsetDateTime suspensionEndDate;
}
