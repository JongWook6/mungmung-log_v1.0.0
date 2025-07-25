package com.grepp.teamnotfound.app.model.recommend.dto;

import com.grepp.teamnotfound.app.model.recommend.code.RecommendState;
import com.grepp.teamnotfound.app.model.recommend.entity.Standard;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendStateDto {

    private RecommendState weightState;
    private RecommendState sleepingState;
    private RecommendState walkingState;

    public static RecommendStateDto toDto(LifeRecordAvgDto avgDto, Standard standard) {
        /**
         * State 기준
         * weight: 15%
         * sleepTime: 20%
         * walking: 40%
         */
        RecommendState weightState = determineState(0.15, avgDto.getAvgWeight(), standard.getMinWeight(), standard.getMaxWeight());
        RecommendState sleepingState = determineState(0.2, avgDto.getAvgSleepTime(), standard.getMinSleep(), standard.getMaxSleep());
        RecommendState walkingState = determineState(0.4, avgDto.getAvgWalking(), standard.getMinWalk(), standard.getMaxWalk());

        return RecommendStateDto.builder()
                .weightState(weightState)
                .sleepingState(sleepingState)
                .walkingState(walkingState)
                .build();
    }

    private static RecommendState determineState(double value, double avg, double min, double max) {

        if (avg < min*(1-value)) return RecommendState.VERY_LOW;

        if (avg < min) return RecommendState.LOW;

        if (avg <= max) return RecommendState.NORMAL;

        if (avg <= max*(1+value)) return RecommendState.HIGH;

        return RecommendState.VERY_HIGH;
    }

}
