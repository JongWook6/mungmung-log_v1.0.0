package com.grepp.teamnotfound.app.controller.api.life_record.payload;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class WalkingResponse {

    private Long walkingId;
    private OffsetDateTime startedAt;
    private OffsetDateTime endedAt;
    private Integer pace;

}
