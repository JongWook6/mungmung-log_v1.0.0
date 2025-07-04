package com.grepp.teamnotfound.app.model.recommend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DailyRecommendDto {

    private Long dailyId;

    @NotNull
    private LocalDate date;

    private Long rec;

    @NotNull
    private Long pet;

}
