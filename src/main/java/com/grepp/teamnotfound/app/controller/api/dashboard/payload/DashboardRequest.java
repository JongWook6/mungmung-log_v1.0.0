package com.grepp.teamnotfound.app.controller.api.dashboard.payload;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DashboardRequest {
    private Long petId;
    private LocalDate date;
}
