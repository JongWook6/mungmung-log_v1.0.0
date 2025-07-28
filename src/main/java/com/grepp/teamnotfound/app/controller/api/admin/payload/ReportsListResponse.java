package com.grepp.teamnotfound.app.controller.api.admin.payload;

import com.grepp.teamnotfound.app.model.report.code.ReportCategory;
import com.grepp.teamnotfound.app.model.report.code.ReportState;
import com.grepp.teamnotfound.app.model.report.code.ReportType;
import com.grepp.teamnotfound.app.model.report.dto.ReportsListDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static com.grepp.teamnotfound.util.TimeUtils.toKST;

@Data
@Builder
public class ReportsListResponse {

    private List<ReportsResponse> reports;
    private PageInfo pageInfo;

    public static ReportsListResponse of(Page<ReportsListDto> reportPage) {
        List<ReportsResponse> reportsResponse = reportPage.getContent().stream()
                .map(ReportsResponse::from)
                .toList();

        return ReportsListResponse.builder()
                .reports(reportsResponse)
                .pageInfo(PageInfo.fromPage(reportPage))
                .build();
    }

    @Getter
    @Builder
    public static class ReportsResponse {
        private Long reportId;
        private String reporter;
        private ReportType type;
        private String reported;
        private LocalDateTime createdAt;
        private ReportCategory category;
        private String reason;
        private ReportState status;

        public static ReportsResponse from(ReportsListDto dto) {
            return ReportsResponse.builder()
                    .reportId(dto.getReportId())
                    .reporter(dto.getReporter())
                    .type(dto.getType())
                    .reported(dto.getReported())
                    .createdAt(toKST(dto.getCreatedAt()))
                    .category(dto.getCategory())
                    .reason(dto.getReason())
                    .status(dto.getStatus())
                    .build();
        }
    }
}
