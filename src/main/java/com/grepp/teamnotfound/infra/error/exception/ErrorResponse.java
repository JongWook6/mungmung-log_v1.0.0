package com.grepp.teamnotfound.infra.error.exception;

import lombok.Getter;

@Getter
public record ErrorResponse(String code, String message) {

}
