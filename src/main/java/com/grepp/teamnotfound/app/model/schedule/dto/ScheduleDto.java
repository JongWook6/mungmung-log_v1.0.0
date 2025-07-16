package com.grepp.teamnotfound.app.model.schedule.dto;

import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ScheduleDto {
    private Long scheduleId;
    private LocalDate date;
    private String name;
    private Boolean isDone;
    private ScheduleCycle cycle;
    private LocalDate cycleEnd;
}
