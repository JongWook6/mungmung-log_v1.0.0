package com.grepp.teamnotfound.app.model.board.repository;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.code.ProfileBoardType;
import com.grepp.teamnotfound.app.model.board.code.SearchType;
import com.grepp.teamnotfound.app.model.board.code.SortType;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import com.grepp.teamnotfound.app.model.board.dto.UserArticleListDto;
import org.springframework.data.domain.Page;

public interface ArticleRepositoryCustom {

    ArticleDetailResponse findDetailById(Long articleId, Long loginUserId);

    Page<ArticleListDto> findArticleListWithMeta(int page, int size, BoardType boardType, SortType sortType, SearchType searchType, String keyword);

    Page<UserArticleListDto> findUserArticleListWithMeta(int page, int size, SortType sortType, ProfileBoardType type, Long userId);
}
