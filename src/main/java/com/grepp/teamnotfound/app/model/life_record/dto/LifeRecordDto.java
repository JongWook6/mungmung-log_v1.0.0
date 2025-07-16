package com.grepp.teamnotfound.app.model.life_record.dto;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordData;
import com.grepp.teamnotfound.app.model.structured_data.dto.FeedingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WalkingDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
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
        LifeRecordDto dto = new LifeRecordDto();
        dto.setLifeRecordId(data.getLifeRecordId());
        dto.setPetId(data.getPetId());
        dto.setRecordAt(data.getRecordAt());
        dto.setContent(data.getContent());
        dto.setWeight(data.getWeight());
        dto.setSleepTime(data.getSleepTime());
        dto.setWalkingList(data.getWalkingList().stream().map(walking -> WalkingDto.builder()
                .startTime(walking.getStartTime())
                .endTime(walking.getEndTime())
                .pace(walking.getPace())
                .build()).toList());
        dto.setFeedingList(data.getFeedingList().stream().map(feeding -> FeedingDto.builder()
                .mealTime(feeding.getMealtime())
                .amount(feeding.getAmount())
                .unit(feeding.getUnit())
                .build()).toList());

        return dto;
    }

}
