package com.grepp.teamnotfound.app.controller.api.article.payload;

import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ArticleListResponse {

    private Integer page;
    private BoardType boardType;
    private List<ArticleListDto> list = new ArrayList<>();
}
