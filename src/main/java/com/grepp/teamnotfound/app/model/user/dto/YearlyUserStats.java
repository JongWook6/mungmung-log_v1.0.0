package com.grepp.teamnotfound.app.model.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class YearlyUserStats {

    private int year;
    private int joinedCount;
    private int leaveCount;
}
