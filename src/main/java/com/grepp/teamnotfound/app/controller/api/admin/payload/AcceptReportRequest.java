package com.grepp.teamnotfound.app.controller.api.admin.payload;

import com.grepp.teamnotfound.app.model.user.code.SuspensionPeriod;
import lombok.Getter;

@Getter
public class AcceptReportRequest {

    private Long reportId;
    private SuspensionPeriod period;
    private String adminReason;
}
