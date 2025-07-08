package com.grepp.teamnotfound.app.controller.api.schedule;

import com.grepp.teamnotfound.app.model.schedule.ScheduleService;
import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleApiController {

    private ScheduleService scheduleService;

    // 일정 조회 시 한달치의 일정 넘기기
    @GetMapping("/{petId}/calendar")
    public ResponseEntity<?> getPetCalendar(
            @PathVariable Long petId,
            @RequestParam Long userId,
            @RequestParam LocalDate date
            ){
        // Mock Data
        List<MockgetPetCalendarDto> mockgetPetCalendarDtos = List.of(
                MockgetPetCalendarDto.builder().scheduleId(1L).date(LocalDate.now().minusDays(1)).name("병원방문").isDone(false).build(),
                MockgetPetCalendarDto.builder().scheduleId(2L).date(LocalDate.now()).name("병원진료").isDone(false).build(),
                MockgetPetCalendarDto.builder().scheduleId(3L).date(LocalDate.now().plusDays(1)).name("병원방문").isDone(false).build()
        );

        return ResponseEntity.ok(Map.of("data", mockgetPetCalendarDtos));
    }

    @PostMapping("{petId}/calendar")
    public ResponseEntity<?> createSchedule(
            @PathVariable Long petId,
            @RequestBody MockCreateScheduleRequestDto requestDto
    ){
        MockCreateScheduleResponseDto responseDto = MockCreateScheduleResponseDto.builder()
                .scheduleId(1L)
                .name("목욕시키는날")
                .date(LocalDate.now().plusDays(3)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("{petId}/calendar")
    public ResponseEntity<?> EditSchedule(
            @PathVariable Long petId,
            @RequestBody MockEditScheduleRequestDto requestDto
    ){
        MockCreateScheduleResponseDto responseDto = MockCreateScheduleResponseDto.builder()
                .scheduleId(1L)
                .name("목욕시키는날")
                .date(LocalDate.now().plusDays(3)).build();

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("{petId}/calendar/delete")
    public ResponseEntity<?> DeleteSchedule(
            @PathVariable Long petId,
            @RequestParam Long userId,
            @RequestParam Long scheduleId
    ){
        return ResponseEntity.ok().build();
    }
}

@Data
@Builder
class MockgetPetCalendarDto {
    private Long scheduleId;
    private LocalDate date;
    private String name;
    private Boolean isDone;
}

@Data
@Builder
class MockCreateScheduleResponseDto {
    private Long scheduleId;
    private LocalDate date;
    private String name;
    private Long petId;
    private ScheduleCycle cycle;
    private LocalDate cycleEnd;
}

@Data
@Builder
class MockCreateScheduleRequestDto {
    private Long userId;
    private Long petId;
    private String name;

    private LocalDate date;

    private ScheduleCycle cycle; // enum 타입 (아래에 정의)

    private LocalDate cycleEnd;
}

@Data
@Builder
class MockEditScheduleRequestDto {
    private Long userId;
    private Long scheduleId;
    private Long petId;
    private String name;

    private LocalDate date;

    private ScheduleCycle cycle; // enum 타입 (아래에 정의)

    private LocalDate cycleEnd;
}