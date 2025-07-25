package com.grepp.teamnotfound.app.controller.api.admin.payload;

import com.grepp.teamnotfound.app.model.user.dto.UsersListDto;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class UsersListResponse {

    private List<UsersListDto> users;
    private PageInfo pageInfo;
}
