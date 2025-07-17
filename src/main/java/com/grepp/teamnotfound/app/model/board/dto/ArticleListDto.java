package com.grepp.teamnotfound.app.model.board.dto;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListDto {

    private Long articleId;
    private String nickname;
    private String profileImgPath;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String title;
    private String content;
    private Integer likes;
    private Integer replies;
    private Integer views;
    private List<ArticleImgDto> articleImgPath;
}
