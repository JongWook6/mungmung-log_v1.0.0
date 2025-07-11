package com.grepp.teamnotfound.app.controller.api.auth.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public record RegisterResponse(Long userId) {

}
