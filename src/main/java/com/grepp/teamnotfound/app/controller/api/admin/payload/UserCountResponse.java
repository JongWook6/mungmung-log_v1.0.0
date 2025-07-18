package com.grepp.teamnotfound.app.controller.api.admin.payload;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class UserCountResponse {

    private OffsetDateTime date;
    private long total;
}
