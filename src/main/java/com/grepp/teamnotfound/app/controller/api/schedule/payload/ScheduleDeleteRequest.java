package com.grepp.teamnotfound.app.controller.api.schedule.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleDeleteRequest {
    private Long userId;
    private Long scheduleId;
    private Boolean cycleLink;
}
