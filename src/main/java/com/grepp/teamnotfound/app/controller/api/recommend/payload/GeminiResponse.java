package com.grepp.teamnotfound.app.controller.api.recommend.payload;

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
        private String status;
        private String recommendation;
    }

}
