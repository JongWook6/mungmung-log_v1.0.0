package com.grepp.teamnotfound.app.controller.api.admin.payload;

import com.grepp.teamnotfound.app.model.report.dto.ReportDetailDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDetailResponse {

    private Long reportId;
    private String reporter;
    private String type;        //board or reply
    private Long contentId;     // articleid or replyid
    private Long articleId;     // articleid
    private String category;    // "ABUSE\" (or \"SPAM\", \"FRAUD\", \"ADULT_CONTENT\""
    private String reason;
    private String status;      //COMPLETE" (or "PENDING")


    public static ReportDetailResponse from(ReportDetailDto dto) {
        return ReportDetailResponse.builder()
                .reportId(dto.getReportId())
                .reporter(dto.getReporter())
                .type(dto.getType())
                .contentId(dto.getContentId())
                .articleId(dto.getArticleId())
                .category(dto.getCategory())
                .reason(dto.getReason())
                .status(dto.getStatus())
                .build();
    }
}
