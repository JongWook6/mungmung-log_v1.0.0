package com.grepp.teamnotfound.app.model.recommend.dto;

import com.grepp.teamnotfound.app.model.liferecord.entity.LifeRecord;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LifeRecordAvgDto {

    private Double avgWeight;
    private Double avgSleepTime;
    private Double avgWalking;

    public static LifeRecordAvgDto toDto(List<LifeRecord> lifeRecordList) {
        List<Double> weightList = new ArrayList<>();
        List<Integer> sleepTimeList = new ArrayList<>();
        List<Integer> walkingList = new ArrayList<>();

        for(LifeRecord lifeRecord : lifeRecordList) {
            if(lifeRecord.getWeight() != null) weightList.add(lifeRecord.getWeight());

            if(lifeRecord.getSleepingTime() != null) sleepTimeList.add(lifeRecord.getSleepingTime());

            if(!lifeRecord.getWalkingList().isEmpty()) {
                walkingList.add(
                        lifeRecord.getWalkingList().stream()
                                .mapToInt(walking -> {
                                    Duration duration = Duration.between(walking.getStartTime(), walking.getEndTime());
                                    return (int) duration.toMinutes();
                                }).sum()
                );
            }
        }

        Double avgWeight = weightList.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        Double avgSleepTime = sleepTimeList.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        Double avgWalking = walkingList.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        return LifeRecordAvgDto.builder()
                .avgWeight(avgWeight)
                .avgSleepTime(avgSleepTime)
                .avgWalking(avgWalking)
                .build();
    }


}
