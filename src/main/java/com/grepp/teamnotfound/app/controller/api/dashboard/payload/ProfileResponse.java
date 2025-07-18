package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private String name;
    private String breed;
    private LocalDate metDay;
    private String aiAnalysis;
}
