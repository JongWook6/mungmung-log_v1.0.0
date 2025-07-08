package com.grepp.teamnotfound.infra.error.exception;

import com.grepp.teamnotfound.infra.error.exception.code.BaseErrorCode;

public class PetException extends CommonException {

    public PetException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
