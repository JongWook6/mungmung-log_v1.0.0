package com.grepp.teamnotfound.app.model.structured_data.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedingDto {

    private Long feedingId;

    @NotNull
    private Double amount;

    @NotNull
    private OffsetDateTime mealTime;

    @NotNull
    @Size(max = 20)
    private String unit;

    @NotNull
    private LocalDate recordedAt;

    @NotNull
    private Long pet;

}
