package com.grepp.teamnotfound.app.model.life_record.dto;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordData;
import com.grepp.teamnotfound.app.model.structured_data.dto.FeedingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WalkingDto;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LifeRecordDto {

    private Long lifeRecordId;
    private Long petId;
    private LocalDate recordAt;

    private String content;
    private Double weight;
    private Integer sleepTime;

    private List<WalkingDto> walkingList;
    private List<FeedingDto> feedingList;

    public static LifeRecordDto toDto(LifeRecordData data) {
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");

        List<WalkingDto> walkingDtos = data.getWalkingList().stream()
                .map(walkingData -> WalkingDto.builder()
                        .startTime(walkingData.getStartTime().atZone(seoulZoneId).toOffsetDateTime())
                        .endTime(walkingData.getEndTime().atZone(seoulZoneId).toOffsetDateTime())
                        .pace(walkingData.getPace())
                        .build()).toList();

        List<FeedingDto> feedingDtos = data.getFeedingList().stream()
                .map(feedingData -> FeedingDto.builder()
                        .mealTime(feedingData.getMealtime().atZone(seoulZoneId).toOffsetDateTime())
                        .amount(feedingData.getAmount())
                        .unit(feedingData.getUnit())
                        .build()).toList();

        return LifeRecordDto.builder()
                .petId(data.getPetId())
                .recordAt(data.getRecordAt())
                .content(data.getContent())
                .sleepTime(data.getSleepTime())
                .weight(data.getWeight())
                .walkingList(walkingDtos)
                .feedingList(feedingDtos)
                .build();
    }

}
