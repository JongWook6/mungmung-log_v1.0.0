package com.grepp.teamnotfound.app.model.dashboard;

import com.grepp.teamnotfound.infra.error.exception.PetException;
import com.grepp.teamnotfound.infra.error.exception.StructuredDataException;
import com.grepp.teamnotfound.infra.error.exception.UserException;
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
    void getProfileFailUser() { // 없는 유저
        assertThrows(UserException.class, () -> dashboardService.getProfile(10000L, 10001L));
    }

    @Test
    void getProfileFailPet() { // 없는 반려견
        assertThrows(PetException.class, () -> dashboardService.getProfile(10001L, 10000L));
    }

    @Test
    void getFeeding() {
        System.out.println(dashboardService.getFeeding(10000L, 10000L, LocalDate.now().minusWeeks(1)));
    }

    @Test
    void getFeedingFail() { // 데이터가 없을 때, 널값 주기
        System.out.println(dashboardService.getFeeding(10000L, 10000L, LocalDate.now().minusMonths(1)));
    }

    @Test
    void getNote() {
        System.out.println(dashboardService.getNote(10000L, 10000L, LocalDate.now().minusWeeks(1)));
    }

    @Test
    void getNoteFail() { // 데이터가 없을 때, 널값 주기
        System.out.println(dashboardService.getNote(10000L, 10000L, LocalDate.now().minusMonths(1)));
    }

    @Test
    void getWalking() {
        System.out.println(dashboardService.getWalking(10000L, 10000L, LocalDate.now().minusWeeks(1)));
    }

    @Test
    void getWalkingFail() { // 데이터가 없을 때, 널값 주기
        System.out.println(dashboardService.getWalking(10000L, 10000L, LocalDate.now().minusMonths(1)));
    }

    @Test
    void getWeight() {
        System.out.println(dashboardService.getWeight(10000L, 10000L, LocalDate.now()));
    }

    @Test
    void getWeightFail() { // 데이터가 없을 때, 빈배열 주기
        System.out.println(dashboardService.getWeight(10000L, 10000L, LocalDate.now().minusMonths(1)));
    }

    @Test
    void getSleeping() {
        System.out.println(dashboardService.getSleeping(10000L, 10000L, LocalDate.now()));
    }

    @Test
    void getSleepingFail() {
        System.out.println(dashboardService.getSleeping(10000L, 10000L, LocalDate.now().minusMonths(1)));
    }
}