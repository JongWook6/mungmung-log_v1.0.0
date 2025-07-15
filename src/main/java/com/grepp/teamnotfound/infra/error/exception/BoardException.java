package com.grepp.teamnotfound.infra.error.exception;

import com.grepp.teamnotfound.infra.error.exception.code.BaseErrorCode;

public class BoardException extends CommonException {

    public BoardException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
