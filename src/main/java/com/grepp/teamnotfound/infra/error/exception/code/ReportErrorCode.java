package com.grepp.teamnotfound.infra.error.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements BaseErrorCode{

    REPORT_REASON_REQUIRED(HttpStatus.BAD_REQUEST.value(), "REPORT_001", "제재 사유를 입력해야 합니다."),
    REPORT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REPORT_002", "존재하지 않는 신고 카테고리입니다."),
    ALREADY_REPORTED_USER(HttpStatus.BAD_REQUEST.value(), "REPORT_003", "이미 제재된 사용자입니다."),
    REPORT_PROCESS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "REPORT_004", "제재 처리에 실패했습니다."),

    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REPORT_005", "존재하지 않는 신고입니다."),

    REPORT_TYPE_BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "REPORT_006", "잘못된 신고 타입입니다."),
    DUPLICATED_REPORT(HttpStatus.BAD_REQUEST.value(), "REPORT_007", "이미 신고한 콘텐츠입니다."),
    CANNOT_REPORT_SELF(HttpStatus.BAD_REQUEST.value(), "REPORT_008", "스스로 작성한 콘텐츠는 신고할 수 없습니다.");


    private final int status;
    private final String code;
    private final String message;
}
