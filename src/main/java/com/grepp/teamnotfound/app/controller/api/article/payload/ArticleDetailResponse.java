package com.grepp.teamnotfound.app.controller.api.article.payload;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ArticleDetailResponse {

    private Long articleId;
    private String writer;
    private String profileImgPath;
    private OffsetDateTime date;
    private String title;
    private String content;
    private Integer replies;
    private Integer likes;
    private Integer views;
    private List<String> articleImgPathList;
}
