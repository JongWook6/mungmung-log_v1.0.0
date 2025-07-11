package com.grepp.teamnotfound.app.controller.api.article.payload;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleDetailResponse {

    private Long articleId;
    private String nickname;
    private String profileImgPath;
    private OffsetDateTime date;
    private String title;
    private String content;
    private Integer replies;
    private Integer likes;
    private Integer views;
    private Boolean isReported;
    private Boolean isLiked;
    private List<String> articleImgPathList;
}
