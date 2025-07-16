package com.grepp.teamnotfound.app.model.dashboard.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WeightDashboardDto {
    List<DayWeight> weightList;

}
