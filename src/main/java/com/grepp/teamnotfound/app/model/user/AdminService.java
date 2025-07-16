package com.grepp.teamnotfound.app.model.user;

import com.grepp.teamnotfound.app.controller.api.admin.payload.UserCountResponse;
import com.grepp.teamnotfound.app.controller.api.admin.payload.UsersListRequest;
import com.grepp.teamnotfound.app.controller.api.admin.payload.UsersListResponse;
import com.grepp.teamnotfound.app.model.user.dto.TotalUsersCount;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public TotalUsersCount getTotalUsersCount() {
        long totalUsers = userRepository.count();
        return TotalUsersCount.builder()
                .date(OffsetDateTime.now())
                .total(totalUsers)
                .build();
    }

    public List<UsersListResponse> getUsersList(UsersListRequest request) {
        return null;
    }
}
