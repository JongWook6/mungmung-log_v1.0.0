package com.grepp.teamnotfound.infra.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OkResponseCode {
    OK("OK", HttpStatus.OK, "정상적으로 완료되었습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;


}
