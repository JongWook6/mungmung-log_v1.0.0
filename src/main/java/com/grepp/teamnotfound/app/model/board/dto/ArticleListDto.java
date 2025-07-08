package com.grepp.teamnotfound.app.model.board.dto;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleListDto {

    private Long articleId;
    private String title;
    private String nickname;
    private Integer replies;
    private Integer views;
    private OffsetDateTime date;
}
