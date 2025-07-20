package com.grepp.teamnotfound.app.controller.api.schedule;

import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleCreateRequest;
import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleEditRequest;
import com.grepp.teamnotfound.app.model.auth.domain.Principal;
import com.grepp.teamnotfound.app.model.schedule.ScheduleService;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleCreateRequestDto;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleDto;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleEditRequestDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/dashboard/v2", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("isAuthenticated()")
public class ScheduleApiController {

    private ScheduleService scheduleService;
    ModelMapper modelMapper = new ModelMapper();

    // 일정 조회 시 한달치의 일정 넘기기
    @GetMapping("/calendar")
    public ResponseEntity<?> getPetCalendar(
            @RequestParam LocalDate date,
            @AuthenticationPrincipal Principal principal
    ){
        List<ScheduleDto> schedules = scheduleService.getCalendar(principal.getUserId(), date);

        return ResponseEntity.ok(Map.of("data", schedules));
    }

    @PostMapping("/calendar")
    public ResponseEntity<?> createSchedule(
            @RequestBody ScheduleCreateRequest request,
            @AuthenticationPrincipal Principal principal
    ){
        modelMapper.getConfiguration().setPropertyCondition(ctx -> !ctx.getMapping().getLastDestinationProperty().getName().equals("petId"));
        ScheduleCreateRequestDto requestDto = new ScheduleCreateRequestDto();
        modelMapper.map(request, requestDto);
        requestDto.setPetId(request.getPetId());
        scheduleService.createSchedule(principal.getUserId(), requestDto);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/calendar")
    public ResponseEntity<?> EditSchedule(
            @RequestBody ScheduleEditRequest request,
            @AuthenticationPrincipal Principal principal
    ){
        modelMapper.getConfiguration().setPropertyCondition(ctx -> !ctx.getMapping().getLastDestinationProperty().getName().equals("petId"));
        ScheduleEditRequestDto requestDto = new ScheduleEditRequestDto();
        modelMapper.map(request, requestDto);
        requestDto.setPetId(request.getPetId());
        scheduleService.editSchedule(principal.getUserId(), requestDto);

        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/calendar/delete")
    public ResponseEntity<?> DeleteSchedule(
            @AuthenticationPrincipal Principal principal,
            @RequestParam Long scheduleId,
            @RequestParam Boolean cycleLink
    ){
        scheduleService.deleteSchedule(principal.getUserId(), scheduleId, cycleLink);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/calendar/{scheduleId}")
    public ResponseEntity<?> ScheduleIsDone(
            @AuthenticationPrincipal Principal principal,
            @PathVariable Long scheduleId
    ){
        scheduleService.checkIsDone(principal.getUserId(), scheduleId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}