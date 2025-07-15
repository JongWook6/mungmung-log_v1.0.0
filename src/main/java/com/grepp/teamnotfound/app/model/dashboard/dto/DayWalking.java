package com.grepp.teamnotfound.app.model.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DayWalking {
    private LocalDate date;
    private Long time;
    private Integer pace;
}
