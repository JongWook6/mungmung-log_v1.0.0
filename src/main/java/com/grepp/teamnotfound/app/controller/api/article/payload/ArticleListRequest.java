package com.grepp.teamnotfound.app.controller.api.article.payload;

import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.code.SearchType;
import com.grepp.teamnotfound.app.model.board.code.SortType;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListRequest {

    @Min(value = 1, message = "페이지는 1 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "페이지 값이 너무 큽니다.")
    private int page = 1;

    @Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "사이즈 값이 너무 큽니다.")
    private int size = 10;

    @NotNull(message = "게시판 타입은 필수입니다.")
    private BoardType boardType;

    @NotNull(message = "정렬 타입은 필수입니다.")
    private SortType sortType;

    private SearchType searchType;
    private String keyword;

//    @QueryProjection
//    public ArticleListRequest(int page, int size, BoardType boardType, SortType sortType,
//        SearchType searchType, String keyword) {
//        this.page = page;
//        this.size = size;
//        this.boardType = boardType;
//        this.sortType = sortType;
//        this.searchType = searchType;
//        this.keyword = keyword;
//    }
}
