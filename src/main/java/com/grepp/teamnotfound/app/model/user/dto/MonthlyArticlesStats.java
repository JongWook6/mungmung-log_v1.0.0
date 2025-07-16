package com.grepp.teamnotfound.app.model.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyArticlesStats {

    private int month;
    private int articlesCount;
}
