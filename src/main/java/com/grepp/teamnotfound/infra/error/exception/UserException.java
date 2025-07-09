package com.grepp.teamnotfound.infra.error.exception;

import com.grepp.teamnotfound.infra.error.exception.code.BaseErrorCode;

public class UserException extends CommonException {
  public UserException(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
