package com.grepp.teamnotfound.app.controller.api.schedule.payload;

import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ScheduleCreateRequest {
    private Long petId;
    private String name;
    private LocalDate date;
    private ScheduleCycle cycle;
    private LocalDate cycleEnd;
}
