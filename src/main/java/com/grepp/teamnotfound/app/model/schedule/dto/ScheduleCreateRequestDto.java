package com.grepp.teamnotfound.app.model.schedule.dto;

import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCreateRequestDto {
    private Long petId;
    private String name;
    private LocalDate date;
    private ScheduleCycle cycle;
    private LocalDate cycleEnd;
}
