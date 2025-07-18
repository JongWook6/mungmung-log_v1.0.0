package com.grepp.teamnotfound.app.model.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyArticlesStatsDto {

    private int month;
    private int articlesCount;
}
