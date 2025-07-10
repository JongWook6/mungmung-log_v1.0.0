package com.grepp.teamnotfound.infra.error.exception;

import com.grepp.teamnotfound.infra.error.exception.code.BaseErrorCode;

public class ScheduleException extends CommonException {

    public ScheduleException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
