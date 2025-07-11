package com.grepp.teamnotfound.app.controller.api.article.payload;

import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ArticleListResponse {

    private List<ArticleListDto> data = new ArrayList<>();
    private PageInfo pageInfo;
}
