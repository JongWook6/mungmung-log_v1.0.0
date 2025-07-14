package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class WalkingResponse {
    private List<DayWalking> walkingList;
}

@Data
@Builder
class DayWalking {
    private LocalDate date;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Integer pace;
}
