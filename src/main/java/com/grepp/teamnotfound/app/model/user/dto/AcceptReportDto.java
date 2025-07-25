package com.grepp.teamnotfound.app.model.user.dto;

import com.grepp.teamnotfound.app.model.user.code.SuspensionPeriod;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AcceptReportDto {

    private Long reportId;
    private SuspensionPeriod period;
    private String adminReason;

}
