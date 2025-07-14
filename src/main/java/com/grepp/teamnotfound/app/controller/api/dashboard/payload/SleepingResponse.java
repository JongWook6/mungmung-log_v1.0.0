package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SleepingResponse {
    private List<DaySleeping> sleepingList;
}

class DaySleeping {
    private LocalDate date;
    private Integer sleep;
}
