package com.grepp.teamnotfound.app.model.recommend.dto;

import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LifeRecordListDto {

    private List<Double> weightList;
    private List<Integer> sleepTimeList;
    private List<Integer> walkingList;

    public static LifeRecordListDto toDto(List<LifeRecord> lifeRecordList) {
        List<Double> weights = new ArrayList<>();
        List<Integer> sleepTimes = new ArrayList<>();
        List<Integer> walkings = new ArrayList<>();

        for(LifeRecord lifeRecord : lifeRecordList) {
            if(lifeRecord.getWeight() != null) {
                weights.add(lifeRecord.getWeight());
            }

            if(lifeRecord.getSleepingTime() != null) {
                sleepTimes.add(lifeRecord.getSleepingTime());
            }

            if(lifeRecord.getWalkingList() != null && !lifeRecord.getWalkingList().isEmpty()) {
                walkings.add(
                    lifeRecord.getWalkingList().stream()
                        .mapToInt(walking -> {
                            Duration duration = Duration.between(walking.getStartTime(), walking.getEndTime());
                            return (int) duration.toMinutes();
                        }).sum()
                );
            }
        }

        return LifeRecordListDto.builder()
                .weightList(weights)
                .sleepTimeList(sleepTimes)
                .walkingList(walkings)
                .build();
    }

}
