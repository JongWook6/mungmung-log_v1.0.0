package com.grepp.teamnotfound.infra.error.exception;

import com.grepp.teamnotfound.infra.error.exception.code.BaseErrorCode;

public class GeminiException extends CommonException {

  public GeminiException(BaseErrorCode errorCode) {
    super(errorCode);
  }
}