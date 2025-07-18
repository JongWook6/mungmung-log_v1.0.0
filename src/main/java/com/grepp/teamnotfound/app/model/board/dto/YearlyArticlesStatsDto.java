package com.grepp.teamnotfound.app.model.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class YearlyArticlesStatsDto {

    private int year;
    private int articlesCount;
}
