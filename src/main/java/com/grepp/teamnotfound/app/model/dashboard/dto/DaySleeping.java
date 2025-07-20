package com.grepp.teamnotfound.app.model.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DaySleeping {
    private LocalDate date;
    private Integer sleep;
}
