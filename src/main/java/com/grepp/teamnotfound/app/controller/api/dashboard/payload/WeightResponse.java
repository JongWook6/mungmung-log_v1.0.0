package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import com.grepp.teamnotfound.app.model.dashboard.dto.DayWeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeightResponse {
    private List<DayWeight> weightList;
}
