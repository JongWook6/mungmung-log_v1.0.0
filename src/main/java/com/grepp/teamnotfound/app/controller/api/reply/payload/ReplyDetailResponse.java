package com.grepp.teamnotfound.app.controller.api.reply.payload;

import com.querydsl.core.annotations.QueryProjection;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ReplyDetailResponse {

    private Long articleId;
    private Long replyId;
    private Long userId;
    private String nickname;
    private String profileImgPath;
    private String content;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @QueryProjection
    public ReplyDetailResponse(
        Long articleId,
        Long replyId,
        Long userId,
        String nickname,
        String profileImgPath,
        String content,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
    ) {
        this.articleId = articleId;
        this.replyId = replyId;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImgPath = profileImgPath;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
