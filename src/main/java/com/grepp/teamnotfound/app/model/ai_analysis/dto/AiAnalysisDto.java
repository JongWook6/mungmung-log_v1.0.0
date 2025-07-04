package com.grepp.teamnotfound.app.model.ai_analysis.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AiAnalysisDto {

    private Long analysisId;

    @NotNull
    private String content;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private Long pet;

}
