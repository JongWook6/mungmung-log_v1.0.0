package com.grepp.teamnotfound.app.model.structured_data.dto;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedingDto {

    private Long feedingId;
    private Pet pet;

    private Double amount;
    private OffsetDateTime mealTime;
    private FeedUnit unit;
    private LocalDate recordedAt;

    private OffsetDateTime createdAt = OffsetDateTime.now();

}
