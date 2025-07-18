package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommendResponse {
    private String recommend;
}
