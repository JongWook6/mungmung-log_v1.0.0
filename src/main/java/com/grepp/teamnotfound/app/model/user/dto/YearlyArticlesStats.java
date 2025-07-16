package com.grepp.teamnotfound.app.model.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class YearlyArticlesStats {

    private int year;
    private int articlesCount;
}
