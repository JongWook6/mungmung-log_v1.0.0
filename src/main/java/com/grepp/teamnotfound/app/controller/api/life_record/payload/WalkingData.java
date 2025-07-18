package com.grepp.teamnotfound.app.controller.api.life_record.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Builder
public class WalkingData {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer pace;

}
