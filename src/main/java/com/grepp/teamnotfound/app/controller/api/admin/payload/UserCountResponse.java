package com.grepp.teamnotfound.app.controller.api.admin.payload;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class UserCountResponse {

    private OffsetDateTime date;
    private long total;
}
