package com.grepp.teamnotfound.app.controller.api.auth.code;

import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.code.AuthErrorCode;

public enum ProviderType {
    GOOGLE, KAKAO, NAVER;

    public static ProviderType from(String input){
        try {
            return ProviderType.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(AuthErrorCode.UNSUPPORTED_PROVIDER);
        }
    }
}
