package com.grepp.teamnotfound.app.model.recommend.dto;

import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.pet.code.PetSize;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class RecommendRequestDto {

    private Long petId;
    private PetType breed;
    private PetSize size;
    private Integer age;

    // 10일 간의 몸무게 리스트
    private List<Double> weightList;
    private Double avgWeight;

    // 10일 간의 수면시간 리스트
    private List<Integer> sleepTimeList;
    private Double avgSleepTime;

    // 10일 간의 산책시간 리스트
    private List<Integer> walkingList;
    private Double avgWalking;

    public static RecommendRequestDto toDto(Pet pet, List<LifeRecord> lifeRecordList) {
        long totalMonths = ChronoUnit.MONTHS.between(pet.getBirthday(), LocalDate.now());
        Integer age = (int) totalMonths;

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

        return RecommendRequestDto.builder()
                .petId(pet.getPetId())
                .breed(pet.getBreed())
                .size(pet.getSize())
                .age(age)
                .weightList(weightList)
                .avgWeight(avgWeight)
                .sleepTimeList(sleepTimeList)
                .avgSleepTime(avgSleepTime)
                .walkingList(walkingList)
                .avgWalking(avgWalking)
                .build();
    }

}
