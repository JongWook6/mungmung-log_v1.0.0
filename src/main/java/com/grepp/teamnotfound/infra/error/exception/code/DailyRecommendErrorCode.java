package com.grepp.teamnotfound.infra.error.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DailyRecommendErrorCode implements BaseErrorCode{

    DAILY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "DAILY_001", "데일리 추천이 존재하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;
}
