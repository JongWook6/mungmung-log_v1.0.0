package com.grepp.teamnotfound.app.controller.api.article.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeResponse {

    private String userEmail;
    private Long articleId;
    private Integer like;
    private Boolean isLiked;
}
