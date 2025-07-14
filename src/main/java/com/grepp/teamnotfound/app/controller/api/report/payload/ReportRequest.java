package com.grepp.teamnotfound.app.controller.api.report.payload;

import com.grepp.teamnotfound.app.model.report.code.ReportCategory;
import com.grepp.teamnotfound.app.model.report.code.ReportType;
import lombok.Data;

@Data
public class ReportRequest {

    private Long reporterId;
    private Long reportedId;
    private ReportType reportType;
    private Long contentId;
    private ReportCategory reportCategory;
    private String reason;
}
