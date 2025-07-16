package com.grepp.teamnotfound.app.model.structured_data.dto;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedingDto {

    private Long feedingId;

    private Double amount;
    private OffsetDateTime mealTime;
    private FeedUnit unit;

}
