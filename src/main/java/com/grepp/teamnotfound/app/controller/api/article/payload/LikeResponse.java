package com.grepp.teamnotfound.app.controller.api.article.payload;

import lombok.Data;

@Data
public class LikeResponse {
    private Integer like;
    private Boolean isLiked;
}
