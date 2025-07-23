package com.grepp.teamnotfound.app.model.report.dto;

import com.grepp.teamnotfound.app.model.report.code.ReportCategory;
import com.grepp.teamnotfound.app.model.report.code.ReportType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportCommand {

    private Long reporterId;        // 신고한 사람(로그인 하는 사람)
    private ReportType reportType;  // BOARD, REPLY
    private Long contentId;
    private ReportCategory reportCategory;
    private String reason;
}
