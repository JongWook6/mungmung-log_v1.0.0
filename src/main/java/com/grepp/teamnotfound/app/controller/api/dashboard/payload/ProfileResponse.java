package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProfileResponse {
    private String name;
    private String breed;
    private LocalDate metDay;
    private String aiAnalysis;
}
