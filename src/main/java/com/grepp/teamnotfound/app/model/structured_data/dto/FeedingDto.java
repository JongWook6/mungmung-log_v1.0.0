package com.grepp.teamnotfound.app.model.structured_data.dto;

import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedingDto {

    private Long feedingId;

    @NotNull
    private Double amount;

    @NotNull
    private OffsetDateTime mealTime;

    @NotNull
    private FeedUnit unit;

    @NotNull
    private LocalDate recordedAt;

    @NotNull
    private Long pet;

    private OffsetDateTime createdAt = OffsetDateTime.now();

}
