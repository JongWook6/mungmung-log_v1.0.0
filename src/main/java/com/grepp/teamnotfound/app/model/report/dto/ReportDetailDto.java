package com.grepp.teamnotfound.app.model.report.dto;

import com.grepp.teamnotfound.app.model.report.entity.Report;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDetailDto {

    private Long reportId;
    private String reporter;    //신고자 닉네임
    private String type;        //board or reply
    private Long contentId;     // articleId or replyId
    private Long articleId;     // articleId or type이 reply일 때 해당 reply의 articleId
    private String category;    // "ABUSE" or "SPAM", "FRAUD", "ADULT_CONTENT"
    private String reason;
    private String status;


    public static ReportDetailDto from(Report report, String reporterNickname, Long articleId) {
        return ReportDetailDto.builder()
                .reportId(report.getReportId())
                .reporter(reporterNickname)
                .type(report.getType().name())
                .contentId(report.getContentId())
                .articleId(articleId)
                .category(report.getCategory().name())
                .reason(report.getReason())
                .status(report.getState().name())
                .build();
    }
}
