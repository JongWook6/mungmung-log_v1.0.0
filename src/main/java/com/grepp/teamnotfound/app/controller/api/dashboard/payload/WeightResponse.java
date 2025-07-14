package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class WeightResponse {
    private List<DayWeight> weightList;
}

@Data
@Builder
class DayWeight {
    private LocalDate date;
    private Double weight;
}
