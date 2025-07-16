package com.grepp.teamnotfound.app.model.notification.handler;

import com.grepp.teamnotfound.app.model.notification.dto.NotiScheduleCreateDto;
import com.grepp.teamnotfound.app.model.user.entity.User;

public interface ScheduleNotiHandler {
    void handle(User user, NotiScheduleCreateDto dto);
}

