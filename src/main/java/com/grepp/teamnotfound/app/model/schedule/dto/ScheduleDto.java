package com.grepp.teamnotfound.app.model.schedule.dto;

import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@Data
@ToString
public class ScheduleDto {
    private Long scheduleId;
    private LocalDate date;
    private Long petId;
    private String petName;
    private String name;
    private Boolean isDone;
    private ScheduleCycle cycle;
    private LocalDate cycleEnd;
}
