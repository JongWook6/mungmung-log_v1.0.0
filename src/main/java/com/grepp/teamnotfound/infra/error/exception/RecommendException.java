package com.grepp.teamnotfound.infra.error.exception;

import com.grepp.teamnotfound.infra.error.exception.code.BaseErrorCode;

public class RecommendException extends CommonException {

    public RecommendException(BaseErrorCode errorCode) {
      super(errorCode);
    }
}