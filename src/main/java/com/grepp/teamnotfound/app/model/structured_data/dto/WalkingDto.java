package com.grepp.teamnotfound.app.model.structured_data.dto;

import com.grepp.teamnotfound.app.model.structured_data.entity.Walking;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalkingDto {

    private Long walkingId;

    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Integer pace;

    public Walking toEntity(){
        Walking walking = new Walking();
        walking.setStartTime(this.startTime);
        walking.setEndTime(this.endTime);
        walking.setPace(this.pace);

        return walking;
    }
}
