package com.grepp.teamnotfound.app.model.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DayWeight {
    private LocalDate date;
    private Double weight;
}
