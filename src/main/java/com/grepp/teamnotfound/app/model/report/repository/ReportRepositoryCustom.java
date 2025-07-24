package com.grepp.teamnotfound.app.model.report.repository;

import com.grepp.teamnotfound.app.controller.api.admin.payload.ReportsListRequest;
import com.grepp.teamnotfound.app.model.report.dto.ReportsListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepositoryCustom {
    Page<ReportsListDto> findReportListWithMeta(ReportsListRequest request, Pageable pageable);

}
