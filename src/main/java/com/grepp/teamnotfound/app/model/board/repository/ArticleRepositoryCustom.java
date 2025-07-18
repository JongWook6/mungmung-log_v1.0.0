package com.grepp.teamnotfound.app.model.board.repository;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.app.model.board.code.SearchType;
import com.grepp.teamnotfound.app.model.board.code.SortType;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import org.springframework.data.domain.Page;

public interface ArticleRepositoryCustom {

    ArticleDetailResponse findDetailById(Long articleId, Long loginUserId);

    Page<ArticleListDto> findArticleListWithMeta(int page, int size, SortType sortType, SearchType searchType, String keyword);
}
