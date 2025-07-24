package com.grepp.teamnotfound.app.controller.api.admin.code;

public enum UsersListSortBy {

    EMAIL,              // 이메일
    NICKNAME,           // 닉네임
    POST_COUNT,         // 게시글 수
    COMMENT_COUNT,      // 댓글 수
    LAST_LOGIN_DATE,    // 최근 접속일
    JOIN_DATE,          // 가입일
    STATE,              // 상태 (asc : 활성 → 정지 → 탈퇴)
    SUSPENSION_END_DATE // 정지 종료일
}
