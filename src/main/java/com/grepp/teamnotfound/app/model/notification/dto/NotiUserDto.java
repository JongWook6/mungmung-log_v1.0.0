package com.grepp.teamnotfound.app.model.notification.dto;

import com.grepp.teamnotfound.app.model.notification.code.NotiType;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotiUserDto {

    Long notiId;
    NotiType type;
    String content;
    Long targetId;
    Boolean isRead;
    OffsetDateTime createdAt;

}
