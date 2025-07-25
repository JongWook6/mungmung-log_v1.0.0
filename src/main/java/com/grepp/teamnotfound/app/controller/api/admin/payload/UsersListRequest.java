package com.grepp.teamnotfound.app.controller.api.admin.payload;

import com.grepp.teamnotfound.app.controller.api.admin.code.AdminListSortDirection;
import com.grepp.teamnotfound.app.controller.api.admin.code.UserStateFilter;
import com.grepp.teamnotfound.app.controller.api.admin.code.UsersListSortBy;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UsersListRequest {

    @Min(value = 1, message="페이지는 1이상이어야 합니다.")
    private int page=1;
    @Min(value = 1, message="사이즈는 1이상이어야 합니다.")
    private int size=5;

    private String search;

    private AdminListSortDirection sort = AdminListSortDirection.ASC;
    private UsersListSortBy sortBy = UsersListSortBy.JOIN_DATE;
    private UserStateFilter status = UserStateFilter.ALL;               // ALL / ACTIVE, SUSPENDED, LEAVE
}
