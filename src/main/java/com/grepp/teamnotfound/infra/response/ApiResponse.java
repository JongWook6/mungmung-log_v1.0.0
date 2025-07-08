package com.grepp.teamnotfound.infra.response;

public record ApiResponse<T>(
        String code,
        String message,
        T data
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(OkResponseCode.OK.getCode(), OkResponseCode.OK.getMessage(), data);
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>("OK", "정상적으로 완료되었습니다.", null);
    }

    public static ApiResponse<?> failure(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

}
