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

    private Double amount;

    private OffsetDateTime mealTime;

    private FeedUnit unit;

    private LocalDate recordedAt;

    private Long pet;

    private OffsetDateTime createdAt = OffsetDateTime.now();

}
