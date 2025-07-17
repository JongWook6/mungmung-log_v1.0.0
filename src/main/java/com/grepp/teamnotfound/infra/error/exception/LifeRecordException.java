package com.grepp.teamnotfound.infra.error.exception;

import com.grepp.teamnotfound.infra.error.exception.code.BaseErrorCode;

public class LifeRecordException extends CommonException {

    public LifeRecordException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
