package com.grepp.teamnotfound.app.controller.api.admin.payload;

import com.grepp.teamnotfound.app.model.user.dto.UsersListDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static com.grepp.teamnotfound.util.TimeUtils.toKST;

@Data
@Builder
public class UsersListResponse {

    private List<UsersResponse> users;
    private PageInfo pageInfo;

    public static UsersListResponse of(Page<UsersListDto> userPage){
        List<UsersResponse> usersResponses = userPage.getContent().stream()
                .map(UsersResponse::from)
                .toList();

        return UsersListResponse.builder()
                .users(usersResponses)
                .pageInfo(PageInfo.fromPage(userPage))
                .build();
    }

    @Getter
    @Builder
    public static class UsersResponse {
        private Long userId;
        private String email;
        private String nickname;
        private Long postCount;
        private Long commentCount;
        private LocalDateTime lastLoginDate;
        private LocalDateTime joinDate;
        private String status;
        private LocalDateTime suspensionEndAt;

        public static UsersResponse from(UsersListDto dto) { // 팩토리 메서드 변경
            return UsersResponse.builder() // 빌더 이름 변경
                    .userId(dto.getUserId())
                    .email(dto.getEmail())
                    .nickname(dto.getNickname())
                    .postCount(dto.getPostCount())
                    .commentCount(dto.getCommentCount())
                    .lastLoginDate(toKST(dto.getLastLoginDate()))
                    .joinDate(toKST(dto.getJoinDate()))
                    .status(dto.getStatus())
                    .suspensionEndAt(toKST(dto.getSuspensionEndAt()))
                    .build();
        }
    }
}
