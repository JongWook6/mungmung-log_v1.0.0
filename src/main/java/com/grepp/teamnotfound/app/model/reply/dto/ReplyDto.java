package com.grepp.teamnotfound.app.model.reply.dto;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class ReplyDto {

    private Long replyId;

    private Long articleId;

    private Long userId;

    private String content;

    private OffsetDateTime createdAt;

}
