package com.grepp.teamnotfound.app.model.board.repository;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;

public interface ArticleRepositoryCustom {

    ArticleDetailResponse findDetailById(Long articleId, Long loginUserId);
}
