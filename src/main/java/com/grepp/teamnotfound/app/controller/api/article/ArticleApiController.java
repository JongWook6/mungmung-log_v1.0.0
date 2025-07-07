package com.grepp.teamnotfound.app.controller.api.article;

import com.grepp.teamnotfound.app.model.board.ArticleService;
import com.grepp.teamnotfound.app.model.board.dto.ArticleDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/articles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleApiController {

    private final ArticleService articleService;

    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        return null;
    }
}
