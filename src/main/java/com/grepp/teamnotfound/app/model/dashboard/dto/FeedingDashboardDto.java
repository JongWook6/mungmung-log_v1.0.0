package com.grepp.teamnotfound.app.model.dashboard.dto;

import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FeedingDashboardDto {
    private LocalDate date;
    private Double amount;
    private Double average;
    private FeedUnit unit;
}
