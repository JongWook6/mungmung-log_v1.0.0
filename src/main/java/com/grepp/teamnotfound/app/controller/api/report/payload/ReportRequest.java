package com.grepp.teamnotfound.app.controller.api.report.payload;

import com.grepp.teamnotfound.app.model.report.code.ReportCategory;
import com.grepp.teamnotfound.app.model.report.code.ReportType;
import lombok.Data;

@Data
public class ReportRequest {

    private Long reporterId;    // 신고한 사람(로그인 하는 사람)
    private Long reportedId;    // 신고당하는 사람(작성자)
    private ReportType reportType;          // BOARD, REPLY
    private Long contentId;
    private ReportCategory reportCategory;  // ABUSE, SPAM, FRAUD, ADULT_CONTENT
    private String reason;
}
