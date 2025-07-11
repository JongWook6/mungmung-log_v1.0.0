package com.grepp.teamnotfound.app.model.reply.dto;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyListDto {

    private Long articleId;
    private Long replyId;
    private String nickname;
    private String content;
    private Boolean isReported;
    private OffsetDateTime date;
}
