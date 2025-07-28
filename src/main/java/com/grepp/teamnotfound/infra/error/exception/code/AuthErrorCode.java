package com.grepp.teamnotfound.infra.error.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode{

    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED.value(), "AUTH_001", "인증에 실패했습니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN.value(), "AUTH_002", "접근 권한이 없습니다."),
    NOT_ADMIN(HttpStatus.FORBIDDEN.value(), "AUTH_003", "관리자 권한이 없습니다."),
    NOT_USER_LOGIN(HttpStatus.FORBIDDEN.value(), "AUTH_004", "일반 사용자 권한이 없습니다."),

    // jwt
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "AUTH_005", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "AUTH_006", "만료된 토큰입니다."),

    // OAuth
    OAUTH_PROVIDER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "AUTH_007", "외부 인증 서버와의 호출에 실패했습니다."),
    SECURITY_INCIDENT(HttpStatus.UNAUTHORIZED.value(),"AUTH_008","토큰 탈취가 의심됩니다." ),

    ALREADY_LOGGED_OUT(HttpStatus.BAD_REQUEST.value(), "AUTH_009", "이미 로그아웃되었습니다."),
    UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST.value(), "AUTH_010", "지원하지 않는 소셜로그인입니다."),

    DELETED_USER(HttpStatus.FORBIDDEN.value(), "AUTH_011", "탈퇴 처리된 사용자입니다.");

    private final int status;
    private final String code;
    private final String message;
}
