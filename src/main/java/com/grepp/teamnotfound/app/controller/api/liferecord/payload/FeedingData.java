package com.grepp.teamnotfound.app.controller.api.liferecord.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grepp.teamnotfound.app.model.structured_data.code.FeedUnit;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Builder
public class FeedingData {

    private Double amount;
    private LocalDateTime mealtime;
    private FeedUnit unit;

}
