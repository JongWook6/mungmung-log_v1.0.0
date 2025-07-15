package com.grepp.teamnotfound.app.model.dashboard.dto;

import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedingDashboardDto {
    private LocalDate date;
    private Double amount;
    private Double average;
    private FeedUnit unit;
}
