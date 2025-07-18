package com.grepp.teamnotfound.app.controller.api.article.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LikeResponse {

    private Long articleId;
    private Integer like;
    private Boolean isLiked;
}
