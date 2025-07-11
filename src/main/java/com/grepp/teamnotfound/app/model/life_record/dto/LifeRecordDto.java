package com.grepp.teamnotfound.app.model.life_record.dto;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordData;
import com.grepp.teamnotfound.app.model.note.dto.NoteDto;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.dto.FeedingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.SleepingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WalkingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WeightDto;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class LifeRecordDto {

    private Long petId;
    private LocalDate recordAt;

    private NoteDto note;
    private SleepingDto sleepTime;
    private WeightDto weight;

    private List<WalkingDto> walkingList;
    private List<FeedingDto> feedingList;

    public LifeRecordDto toDto(LifeRecordData data, Pet pet){
        LifeRecordDto dto = new LifeRecordDto();
        dto.setPetId(data.getPetId());
        dto.setRecordAt(data.getRecordAt());
        dto.setNote(NoteDto.builder()
                .content(data.getContent())
                .recordedAt(data.getRecordAt())
                .pet(pet).build());
        dto.setSleepTime(SleepingDto.builder()
                .sleepingTime(data.getSleepTime())
                .recordedAt(data.getRecordAt())
                .pet(pet).build());
        dto.setWeight(WeightDto.builder()
                .weight(data.getWeight())
                .recordedAt(data.getRecordAt())
                .pet(pet).build());
        dto.setWalkingList(data.getWalkingList().stream().map(walking -> WalkingDto.builder()
                .startedAt(walking.getStartedAt())
                .endedAt(walking.getEndedAt())
                .pace(walking.getPace())
                .recordedAt(data.getRecordAt())
                .pet(pet)
                .build()).toList());
        dto.setFeedingList(data.getFeedingList().stream().map(feeding -> FeedingDto.builder()
                .mealTime(feeding.getMealtime())
                .amount(feeding.getAmount())
                .unit(feeding.getUnit())
                .recordedAt(data.getRecordAt())
                .pet(pet)
                .build()).toList());

        return dto;
    }

}
