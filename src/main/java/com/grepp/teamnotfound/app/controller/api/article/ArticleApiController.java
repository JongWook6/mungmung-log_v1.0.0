package com.grepp.teamnotfound.app.controller.api.article;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleListResponse;
import com.grepp.teamnotfound.app.model.board.ArticleService;
import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.dto.ArticleDto;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/community/articles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleApiController {

    private final ArticleService articleService;

    // 특정 게시판의 게시글 리스트 조회
    @GetMapping
    public ResponseEntity<ArticleListResponse> getAllArticles() {
        ArticleListResponse response = new ArticleListResponse();
        response.setPage(1);
        response.setBoardType(BoardType.FREE);

        for (int i = 1; i <= 5; i++) {
            ArticleListDto dto = ArticleListDto.builder()
                .articleId(Integer.toUnsignedLong(i))
                .title("마음이의 산책 일상 " + i)
                .writer("사용자 " + i)
                .replies(i)
                .views(i)
                .date(OffsetDateTime.now())
                .build();
            response.getList().add(dto);
        }
        return ResponseEntity.ok(response);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticle(
        @RequestParam Long id
    ) {


        return null;
    }

}
