package com.grepp.teamnotfound.app.model.recommend.dto;

import com.grepp.teamnotfound.app.model.pet.code.PetPhase;
import com.grepp.teamnotfound.app.model.pet.code.PetSize;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import com.grepp.teamnotfound.app.model.recommend.code.RecommendState;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class RecommendDto {

    private PetType breed;
    private PetSize size;
    private PetPhase age;
    private RecommendState weightState;
    private RecommendState walkingState;
    private RecommendState sleepingState;
    private String content;

    // 데이터를 저장할 때 사용
    public static RecommendDto toDto(PetInfoDto petInfoDto, RecommendStateDto stateDto, GeminiResponse response) {
        String content = String.join("\n",
            response.getWeight().getRecommendation(),
            response.getWalk().getRecommendation(),
            response.getSleep().getRecommendation()
        );

        return RecommendDto.builder()
                .breed(petInfoDto.getBreed())
                .size(petInfoDto.getSize())
                .age(petInfoDto.getAge())
                .weightState(stateDto.getWeightState())
                .walkingState(stateDto.getWalkingState())
                .sleepingState(stateDto.getSleepingState())
                .content(content)
                .build();
    }

}
