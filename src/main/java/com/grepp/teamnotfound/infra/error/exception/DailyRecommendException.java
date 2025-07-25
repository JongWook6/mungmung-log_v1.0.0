package com.grepp.teamnotfound.infra.error.exception;

import com.grepp.teamnotfound.infra.error.exception.code.BaseErrorCode;

public class DailyRecommendException extends CommonException {

    public DailyRecommendException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}