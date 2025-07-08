package com.grepp.teamnotfound.app.controller.api.life_record.payload;

import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class FeedingResponse {

    private Long feedingId;
    private Double amount;
    private OffsetDateTime mealtime;
    private FeedUnit unit;

}
