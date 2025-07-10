package com.grepp.teamnotfound.app.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyEmailRequestDto {

        private String email;
        private String verificationCode;

    }
