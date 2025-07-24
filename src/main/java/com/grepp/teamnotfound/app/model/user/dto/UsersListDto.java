package com.grepp.teamnotfound.app.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersListDto {

    private Long userId;
    private String email;
    private String nickname;
    private Long postCount;
    private Long commentCount;
    private OffsetDateTime lastLoginDate;
    private OffsetDateTime joinDate;
    private String status;
    private OffsetDateTime suspensionEndAt;

}
