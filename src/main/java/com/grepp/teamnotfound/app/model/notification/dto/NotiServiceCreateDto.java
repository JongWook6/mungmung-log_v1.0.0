package com.grepp.teamnotfound.app.model.notification.dto;

import com.grepp.teamnotfound.app.model.notification.code.NotiType;
import lombok.Data;

@Data
public class NotiServiceCreateDto implements NotiBasicDto{
    private Long targetId;
    private NotiType type;

    public NotiServiceCreateDto(NotiType type) {
        this.type = type;
    }

    @Override
    public NotiType getType() {
        return this.type;
    }

}
