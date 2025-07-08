package com.grepp.teamnotfound.app.model.schedule.code;

import java.time.LocalDate;
import java.time.Period;

public enum ScheduleCycle {
    WEEK, ONE_MONTH, THREE_MONTH, SIX_MONTH, YEAR;

    public int getDays(LocalDate date){
        return switch (this) {
            case WEEK -> 7;
            case ONE_MONTH -> Period.between(date, date.plusMonths(1)).getDays();
            case THREE_MONTH -> Period.between(date, date.plusMonths(3)).getDays();
            case SIX_MONTH -> Period.between(date, date.plusMonths(6)).getDays();
            case YEAR -> Period.between(date, date.plusYears(1)).getDays();
            default -> 0;
        };
    }
}
