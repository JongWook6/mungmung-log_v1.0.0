package com.grepp.teamnotfound.app.controller.api.recommend.payload;

import com.grepp.teamnotfound.app.model.recommend.code.RecommendState;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiResponse;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class RecommendResponse {

    private LocalDate date;
    private RecommendState weightState;
    private RecommendState walkingState;
    private RecommendState sleepingState;
    private String content;

    // 데이터를 저장할 때 사용
    public static RecommendResponse toResponse(GeminiResponse response) {
        String content = String.join("\n",
            response.getWeight().getRecommendation(),
            response.getWalk().getRecommendation(),
            response.getSleep().getRecommendation()
        );

        return RecommendResponse.builder()
                .date(LocalDate.now())
                .weightState(response.getWeight().getStatus())
                .walkingState(response.getWalk().getStatus())
                .sleepingState(response.getSleep().getStatus())
                .content(content)
                .build();
    }

    // 기존 데이터 보여줄 때 사용
    public static RecommendResponse toResponse(Recommend recommend) {
        return RecommendResponse.builder()
                .date(recommend.getDate())
                .weightState(recommend.getWeightState())
                .walkingState(recommend.getWalkingState())
                .sleepingState(recommend.getSleepingState())
                .content(recommend.getContent())
                .build();
    }

}
