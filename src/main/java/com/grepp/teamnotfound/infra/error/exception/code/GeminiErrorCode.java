package com.grepp.teamnotfound.infra.error.exception.code;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GeminiErrorCode implements BaseErrorCode {

    GEMINI_REQUIRED_CONTENT(HttpStatus.BAD_GATEWAY.value(), "GEMINI_001", "응답에 필요한 컨텐츠가 누락되었습니다"),
    GEMINI_PARSING_ERROR(HttpStatus.BAD_GATEWAY.value(), "GEMINI_001", "Gemini 응답 Parsing 실패");

    private final int status;
    private final String code;
    private final String message;
}