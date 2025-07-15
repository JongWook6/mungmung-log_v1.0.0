package com.grepp.teamnotfound.app.controller.api.article.payload;

import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.code.SearchType;
import com.grepp.teamnotfound.app.model.board.code.SortType;
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
    private int page;

    @Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
    private int size;

    @NotNull(message = "게시판 타입은 필수입니다.")
    private BoardType boardType;

    @NotNull(message = "정렬 타입은 필수입니다.")
    private SortType sortType;

    private SearchType searchType;
    private String keyword;
}
