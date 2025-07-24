package com.grepp.teamnotfound.app.model.report.dto;

import com.grepp.teamnotfound.app.model.report.code.ReportCategory;
import com.grepp.teamnotfound.app.model.report.code.ReportState;
import com.grepp.teamnotfound.app.model.report.code.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ReportsListDto {

    private Long reportId;
    private String reporter;
    private ReportType type;
    private String reported;
    private OffsetDateTime createdAt;
    private ReportCategory category;
    private String reason;
    private ReportState status;
}
