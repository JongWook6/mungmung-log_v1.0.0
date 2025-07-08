package com.grepp.teamnotfound.infra.response;

public record ApiResponse<T>(
        String code,
        String message,
        T data
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(OkResponseCode.OK.getCode(), OkResponseCode.OK.getMessage(), data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(OkResponseCode.OK.getCode(), message, null);
    }

    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(OkResponseCode.OK.getCode(), OkResponseCode.OK.getMessage(), null);
    }

    public static ApiResponse<?> error(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

}
