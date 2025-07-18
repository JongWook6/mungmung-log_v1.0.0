package com.grepp.teamnotfound.app.controller.api.life_record.payload;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LifeRecordData {

    private Long lifeRecordId;

    private Long petId;
    private LocalDate recordAt;

    private String content;

    private Integer sleepTime;

    private Double weight;

    // 산책
    private List<WalkingData> walkingList;

    // 식사량
    private List<FeedingData> feedingList;

}
