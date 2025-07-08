package com.grepp.teamnotfound.infra.response;

public record ApiResponse<T>(
        String code,
        String message
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(OkResponseCode.OK.getCode(), OkResponseCode.OK.getMessage());
    }

}
