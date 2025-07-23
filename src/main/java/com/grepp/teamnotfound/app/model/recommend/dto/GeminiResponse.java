package com.grepp.teamnotfound.app.model.recommend.dto;

import com.grepp.teamnotfound.app.model.recommend.code.RecommendState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeminiResponse {

    private Recommendation weight;
    private Recommendation sleep;
    private Recommendation walk;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recommendation {
        private RecommendState status;
        private String recommendation;
    }

}
