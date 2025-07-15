package com.grepp.teamnotfound.app.model.schedule.code;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum ScheduleCycle {
    @JsonEnumDefaultValue // 모르는 값은 NONE 처리
    NONE,
    WEEK, ONE_MONTH, THREE_MONTH, SIX_MONTH, YEAR;

    public int getDays(LocalDate date){
        return switch (this) {
            case WEEK -> 7;
            case ONE_MONTH -> (int) ChronoUnit.DAYS.between(date, date.plusMonths(1));
            case THREE_MONTH -> (int) ChronoUnit.DAYS.between(date, date.plusMonths(3));
            case SIX_MONTH -> (int) ChronoUnit.DAYS.between(date, date.plusMonths(6));
            case YEAR -> (int) ChronoUnit.DAYS.between(date, date.plusYears(1));
            default -> throw new IllegalArgumentException();
        };
    }
}
