package com.grepp.teamnotfound.infra.error.exception.advice;

import com.grepp.teamnotfound.infra.error.exception.CommonException;
import com.grepp.teamnotfound.infra.error.exception.code.AuthErrorCode;
import com.grepp.teamnotfound.infra.error.exception.dto.ErrorResponse;
import com.grepp.teamnotfound.infra.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException e) {

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(
                        e.getErrorCode().getStatus(),
                        e.getErrorCode().getCode(),
                        e.getErrorCode().getMessage(),
                        LocalDateTime.now()));
    }

    // 유효성 검증 @Valid Exception catch
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();

        String errorMessage = bindingResult.getFieldErrors().getFirst().getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "VALIDATION_ERROR",
                        errorMessage,
                        LocalDateTime.now()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
        FieldError fieldError = ex.getFieldError();

        if (fieldError != null) {
            String field = fieldError.getField();
            Object rejectedValue = fieldError.getRejectedValue();
            String message;

            if ("boardType".equals(field) || "sortType".equals(field)) {
                message = String.format(
                    "파라미터 '%s'의 값 '%s'은(는) 허용되지 않는 값입니다. 올바른 값을 입력해 주세요.",
                    field,
                    rejectedValue
                );
            } else {
                message = fieldError.getDefaultMessage(); // 기본 메시지
            }

            return ResponseEntity.badRequest().body(
                new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "ENUM_TYPE_ERROR",
                    message,
                    LocalDateTime.now()
                )
            );
        }

        // 기본 fallback
        return ResponseEntity.badRequest().body(
            new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_REQUEST",
                "요청 파라미터가 올바르지 않습니다.",
                LocalDateTime.now()
            )
        );
    }

    @ExceptionHandler({AuthorizationDeniedException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException() {
          return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(
                        HttpStatus.FORBIDDEN.value(),
                        "AUTH_002",
                        "접근 권한이 없습니다.",
                        LocalDateTime.now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        "USER_002",
                        "아이디 또는 비밀번호가 일치하지 않습니다.",
                        LocalDateTime.now()
                ));
    }

    // 그 외 모든 Exception catch
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception occurs: {}", e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "내부 서버에 예상치 못한 오류가 발생했습니다.",
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
