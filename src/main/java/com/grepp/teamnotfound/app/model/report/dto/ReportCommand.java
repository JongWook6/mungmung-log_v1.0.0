package com.grepp.teamnotfound.app.model.report.dto;

import com.grepp.teamnotfound.app.model.report.code.ReportCategory;
import com.grepp.teamnotfound.app.model.report.code.ReportType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportCommand {

    private Long reporterId;
//    private Long  reportedId;
    private ReportType reportType;
    private Long contentId;
    private ReportCategory reportCategory;
    private String reason;
}
