package com.grepp.teamnotfound.infra.error.exception;

import com.grepp.teamnotfound.infra.error.exception.code.BaseErrorCode;

public class StandardException extends CommonException {

    public StandardException(BaseErrorCode errorCode) { super(errorCode); }
}
