package com.grepp.teamnotfound.app.controller.api.admin.code;

public enum ReportsListSortBy {

    REPORTED_AT,        // 접수일
    REPORTER_NICKNAME,  // 신고자
    REPORTED_NICKNAME,  // 피신고자
    CONTENT_TYPE,       // 콘텐츠 유형 (BOARD → REPLY)
    REASON,             // 신고 이유
    STATUS              // 처리 상태 (PENDING → ACCEPT → REJECT)
}
