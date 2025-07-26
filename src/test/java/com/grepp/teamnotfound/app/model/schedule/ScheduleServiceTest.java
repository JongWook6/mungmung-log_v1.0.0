package com.grepp.teamnotfound.app.model.schedule;

import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleCreateRequest;
import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleEditRequest;
import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleCreateRequestDto;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleDto;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleEditRequestDto;
import com.grepp.teamnotfound.app.model.schedule.entity.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;

    // 일정 조회
    @Test
    void getCalendar() {
        List<ScheduleDto> schedules = scheduleService.getCalendar(10000L, LocalDate.now());
        System.out.println(schedules);
    }

    // 반복 없는 일정 등록
    @Test
    void createSchedule() {
        ScheduleCreateRequestDto request = ScheduleCreateRequestDto.builder()
                .petId(10000L)
                .name("병원가는 날")
                .cycle(ScheduleCycle.NONE)
                .cycleEnd(LocalDate.now())
                .date(LocalDate.now())
                .build();
        scheduleService.createSchedule(10000L, request);
    }

    // 반복 있는 일정 등록
    @Test
    void createCycleSchedule() {
        ScheduleCreateRequestDto request = ScheduleCreateRequestDto.builder()
                .petId(10000L)
                .name("목욕하는 날")
                .date(LocalDate.now())
                .cycle(ScheduleCycle.WEEK)
                .cycleEnd(LocalDate.now().plusMonths(1)).build();
        scheduleService.createSchedule(10000L, request);
    }

    // 반복 없는 일정 수정
    @Test
    void editSchedule() {
        ScheduleEditRequestDto request = ScheduleEditRequestDto.builder()
                .petId(10000L).scheduleId(10025L)
                .date(LocalDate.now().plusDays(2))
                .name("병원가는날 수정")
                .cycleLink(false)
                .build();
        scheduleService.editSchedule(10000L, request);
    }

    // 반복 있는 일정 수정
    @Test
    void editCycleSchedule1() {
        ScheduleEditRequestDto request = ScheduleEditRequestDto.builder()
                .petId(10000L).scheduleId(10014L)
                .date(LocalDate.now().plusDays(2))
                .name("목욕하는 날")
                .cycleLink(true)
                .cycle(ScheduleCycle.ONE_MONTH)
                .cycleEnd(LocalDate.now().plusMonths(5))
                .build();
        scheduleService.editSchedule(10000L, request);
    }

    // 반복 있는 일정 수정
    @Test
    void editCycleSchedule2() {
        ScheduleEditRequestDto request = ScheduleEditRequestDto.builder()
                .petId(10000L).scheduleId(10014L)
                .date(LocalDate.now().plusDays(2))
                .name("목욕하는 날 수정")
                .cycleLink(true)
                .cycle(ScheduleCycle.WEEK)
                .cycleEnd(LocalDate.now().plusMonths(5))
                .build();
        scheduleService.editSchedule(10000L, request);
    }

    // 반복 없는 일정 반복 있게 수정
    @Test
    void editCycleSchedule3() {
        ScheduleEditRequestDto request = ScheduleEditRequestDto.builder()
                .petId(10000L).scheduleId(10013L)
                .date(LocalDate.now().plusDays(2))
                .name("string aaa")
                .cycleLink(true)
                .cycle(ScheduleCycle.WEEK)
                .cycleEnd(LocalDate.now().plusMonths(1))
                .build();
        scheduleService.editSchedule(10000L, request);
    }

    // 반복 있는 일정 반복 없게 수정
    @Test
    void editCycleSchedule4() {
        ScheduleEditRequestDto request = ScheduleEditRequestDto.builder()
                .petId(10000L).scheduleId(10013L)
                .date(LocalDate.now().plusDays(2))
                .name("string aaa")
                .cycleLink(true)
                .cycle(ScheduleCycle.NONE)
                .cycleEnd(LocalDate.now().plusDays(2))
                .build();
        scheduleService.editSchedule(10000L, request);
    }

    // 반복 없는 일정 삭제
    @Test
    void deleteSchedule() {
        scheduleService.deleteSchedule(10025L, 10000L, false);
    }

    // 반복 있는 일정 삭제
    @Test
    void deleteCycleSchedule() {
        scheduleService.deleteSchedule(10014L, 10000L, true);
    }

    // 일정완료 여부
    @Test
    void checkIsDone(){
        scheduleService.checkIsDone(10000L, 10014L);
    }
}