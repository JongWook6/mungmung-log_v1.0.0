package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FeedingResponse {
    private LocalDate date;
    private Double DayAmount;
    private Double WeekAmount;
}
