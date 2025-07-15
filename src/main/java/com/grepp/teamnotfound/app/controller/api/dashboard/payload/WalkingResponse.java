package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import com.grepp.teamnotfound.app.model.dashboard.dto.DayWalking;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WalkingResponse {
    private List<DayWalking> walkingList;
}