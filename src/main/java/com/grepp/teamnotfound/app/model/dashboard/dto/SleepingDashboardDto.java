package com.grepp.teamnotfound.app.model.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SleepingDashboardDto {
    private List<DaySleeping> sleepingList;
}
