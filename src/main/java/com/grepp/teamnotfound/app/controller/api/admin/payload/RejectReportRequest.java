package com.grepp.teamnotfound.app.controller.api.admin.payload;

import lombok.Getter;

@Getter
public class RejectReportRequest {

    private Long reportId;
    private String adminReason;
}
