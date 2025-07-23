package com.grepp.teamnotfound.app.model.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RejectReportDto {

    private Long reportId;
    private String adminReason;
}
