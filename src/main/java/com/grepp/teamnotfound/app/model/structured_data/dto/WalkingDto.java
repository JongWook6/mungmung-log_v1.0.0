package com.grepp.teamnotfound.app.model.structured_data.dto;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.WalkingData;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalkingDto {

    private Long walkingId;

    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Integer pace;

}
