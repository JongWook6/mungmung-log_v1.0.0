package com.grepp.teamnotfound.app.controller.api.life_record.payload;

import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private List<WalkingData> walkingList;
    private List<FeedingData> feedingList;

    public static LifeRecordData of(LifeRecord lifeRecord){
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");

        List<WalkingData> walkingList = lifeRecord.getWalkingList().stream()
                .map(walking -> WalkingData.builder()
                        .startTime(walking.getStartTime().atZoneSameInstant(seoulZoneId).toLocalDateTime())
                        .endTime(walking.getEndTime().atZoneSameInstant(seoulZoneId).toLocalDateTime())
                        .pace(walking.getPace())
                        .build()).toList();
        List<FeedingData> feedingList = lifeRecord.getFeedingList().stream()
                .map(feeding -> FeedingData.builder()
                        .mealtime(feeding.getMealTime().atZoneSameInstant(seoulZoneId).toLocalDateTime())
                        .amount(feeding.getAmount())
                        .unit(feeding.getUnit())
                        .build()).toList();

        return LifeRecordData.builder()
                .lifeRecordId(lifeRecord.getLifeRecordId())
                .petId(lifeRecord.getPet().getPetId())
                .recordAt(lifeRecord.getRecordedAt())
                .content(lifeRecord.getContent())
                .weight(lifeRecord.getWeight())
                .sleepTime(lifeRecord.getSleepingTime())
                .walkingList(walkingList)
                .feedingList(feedingList)
                .build();
    }

}
