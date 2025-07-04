package com.grepp.teamnotfound.app.model.reply.dto;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class ReplyDto {

    private Long replyId;

    private Long boardId;

    private Long userId;

    private String content;

    private OffsetDateTime createdAt;

}
