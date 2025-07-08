package com.grepp.teamnotfound.app.model.schedule;

import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleCreateRequest;
import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import com.grepp.teamnotfound.app.model.schedule.entity.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;

    // 일정 조회
    @Test
    void getCalendar() {
        List<Schedule> schedules = scheduleService.getCalendar(1L, LocalDate.now());
        assertNotNull(schedules);
        assertTrue(schedules.size() >= 0);
    }

    // 반복 없는 일정 등록
    @Test
    void createSchedule() {
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .userId(1L)
                .petId(1L)
                .name("병원가는 날")
                .date(LocalDate.now())
                .build();
        scheduleService.createSchedule(request);
        assertTrue(true);
    }

    // 반복 있는 일정 등록
    @Test
    void createCycleSchedule() {
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .userId(1L)
                .petId(1L)
                .name("목욕하는 날")
                .date(LocalDate.now())
                .cycle(ScheduleCycle.WEEK)
                .cycleEnd(LocalDate.now().plusMonths(1)).build();
        scheduleService.createSchedule(request);
        assertTrue(true);
    }

    // 반복 없는 일정 수정
    @Test
    void editSchedule() {

    }

    // 반복 있는 일정 수정
    @Test
    void editCycleSchedule() {

    }

    // 반복 없는 일정 삭제
    @Test
    void deleteSchedule() {
    }

    // 반복 있는 일정 삭제
    @Test
    void deleteCycleSchedule() {
    }
}