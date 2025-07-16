package com.grepp.teamnotfound.app.model.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyUserStats {

    private int month;
    private int joinedCount;
    private int leaveCount;
}
