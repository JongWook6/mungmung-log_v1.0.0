package com.grepp.teamnotfound.app.model.notification.handler;

import com.grepp.teamnotfound.app.model.notification.dto.NotiServiceCreateDto;
import com.grepp.teamnotfound.app.model.user.entity.User;

public interface ServiceNotiHandler {
    void handle(User user, NotiServiceCreateDto dto);
}
