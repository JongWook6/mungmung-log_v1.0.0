package com.grepp.teamnotfound.app.model.board.dto;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ArticleDto {

    private Long articleId;

    private Long userId;

    private Long boardId;

    private String title;

    private String content;

    private Integer views;

    private OffsetDateTime reportedAt;

    private OffsetDateTime createdAt;

    private List<ArticleImgDto> images;
}
