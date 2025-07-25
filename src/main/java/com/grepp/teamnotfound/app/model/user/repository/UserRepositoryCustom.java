package com.grepp.teamnotfound.app.model.user.repository;

import com.grepp.teamnotfound.app.controller.api.admin.payload.UsersListRequest;
import com.grepp.teamnotfound.app.model.user.dto.UsersListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<UsersListDto> findUserListWithMeta(UsersListRequest request, Pageable pageable);
}
