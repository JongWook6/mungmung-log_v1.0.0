package com.grepp.teamnotfound.app.controller.api.admin.payload;

import com.grepp.teamnotfound.app.model.report.dto.ReportsListDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReportsListResponse {

    private List<ReportsListDto> reports;
    private PageInfo pageInfo;
}
