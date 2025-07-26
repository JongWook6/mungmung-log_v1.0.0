package com.grepp.teamnotfound.infra.error.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LifeRecordErrorCode implements BaseErrorCode{

    LIFERECORD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "LIFERECORD_001","존재하지 않는 생활기록입니다."),
    LIFERECORD_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "LIFERECORD_002", "해당 생활기록에 대한 접근 권한이 없습니다."),
    FEEDING_UNIT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "FEEDING_001", "식사 단위가 존재하지 않습니다.");


    private final int status;
    private final String code;
    private final String message;
}
