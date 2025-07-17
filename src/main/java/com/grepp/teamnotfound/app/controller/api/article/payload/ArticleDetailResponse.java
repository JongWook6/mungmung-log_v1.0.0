package com.grepp.teamnotfound.app.controller.api.article.payload;

import com.grepp.teamnotfound.app.model.board.dto.ArticleImgDto;
import com.querydsl.core.annotations.QueryProjection;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
public class ArticleDetailResponse {

    private Long articleId;
    private String nickname;
    private String profileImgPath;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String title;
    private String content;
    private Integer replies;
    private Integer likes;
    private Integer views;
    private Boolean isLiked;
    private List<ArticleImgDto> images;

    @QueryProjection
    public ArticleDetailResponse(
        Long articleId,
        String nickname,
        String profileImgPath,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String title,
        String content,
        Integer replies,
        Integer likes,
        Integer views,
        Boolean isLiked,
        List<ArticleImgDto> images
    ) {
        this.articleId = articleId;
        this.nickname = nickname;
        this.profileImgPath = profileImgPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.content = content;
        this.replies = replies;
        this.likes = likes;
        this.views = views;
        this.isLiked = isLiked;
        this.images = images;
    }
}
