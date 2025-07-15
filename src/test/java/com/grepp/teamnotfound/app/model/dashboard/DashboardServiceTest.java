package com.grepp.teamnotfound.app.model.dashboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DashboardServiceTest {
    @Autowired
    private DashboardService dashboardService;


    @Test
    void getRecommend() {

    }

    @Test
    void getProfile() {
        System.out.println(dashboardService.getProfile(10000L, 10000L));
    }

    @Test
    void getFeeding() {
        System.out.println(dashboardService.getFeeding(10000L, 10000L, LocalDate.now().minusWeeks(1)));
    }

    @Test
    void getNote() {
        System.out.println(dashboardService.getNote(10000L, 10000L, LocalDate.now().minusWeeks(1)));
    }

    @Test
    void getWalking() {
        System.out.println(dashboardService.getWalking(10000L, 10000L, LocalDate.now().minusWeeks(1)));
    }

    @Test
    void getWeight() {
        System.out.println(dashboardService.getWeight(10000L, 10000L, LocalDate.now()));
    }

    @Test
    void getSleeping() {
        System.out.println(dashboardService.getSleeping(10000L, 10000L, LocalDate.now()));

    }
}