package com.grepp.teamnotfound.app.controller.api.schedule;

import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleCreateRequest;
import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleEditRequest;
import com.grepp.teamnotfound.app.model.schedule.ScheduleService;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleCreateRequestDto;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleDto;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleEditRequestDto;
import com.grepp.teamnotfound.app.model.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleApiController {

    private ScheduleService scheduleService;
    ModelMapper modelMapper = new ModelMapper();

    // 일정 조회 시 한달치의 일정 넘기기
    @GetMapping("/{userId}/calendar")
    public ResponseEntity<?> getPetCalendar(
            @RequestParam Long userId,
            @RequestParam LocalDate date
            ){
        List<ScheduleDto> schedules = scheduleService.getCalendar(userId, date);

        return ResponseEntity.ok(Map.of("data", schedules));
    }

    @PostMapping("{petId}/calendar")
    public ResponseEntity<?> createSchedule(
            @PathVariable Long petId,
            @RequestBody ScheduleCreateRequest request
    ){
        modelMapper.getConfiguration().setPropertyCondition(ctx -> !ctx.getMapping().getLastDestinationProperty().getName().equals("petId"));
        ScheduleCreateRequestDto requestDto = new ScheduleCreateRequestDto();
        modelMapper.map(request, requestDto);
        requestDto.setPetId(petId);
        scheduleService.createSchedule(requestDto);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("{petId}/calendar")
    public ResponseEntity<?> EditSchedule(
            @PathVariable Long petId,
            @RequestBody ScheduleEditRequest request
    ){
        modelMapper.getConfiguration().setPropertyCondition(ctx -> !ctx.getMapping().getLastDestinationProperty().getName().equals("petId"));
        ScheduleEditRequestDto requestDto = new ScheduleEditRequestDto();
        modelMapper.map(request, requestDto);
        requestDto.setPetId(petId);
        scheduleService.editSchedule(requestDto);

        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("{petId}/calendar/delete")
    public ResponseEntity<?> DeleteSchedule(
            @PathVariable Long petId,
            @RequestParam Long userId,
            @RequestParam Long scheduleId,
            @RequestParam Boolean cycleLink
    ){
        scheduleService.deleteSchedule(userId, scheduleId, cycleLink);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("{petId}/calendar/{scheduleId}")
    public ResponseEntity<?> ScheduleIsDone(
            @PathVariable Long petId,
            @PathVariable Long scheduleId
    ){
        scheduleService.checkIsDone(petId, scheduleId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}