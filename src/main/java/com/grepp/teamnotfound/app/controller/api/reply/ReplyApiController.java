package com.grepp.teamnotfound.app.controller.api.reply;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleListResponse;
import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import java.time.OffsetDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class ReplyApiController {

    // 댓글 리스트
    @GetMapping
    public ResponseEntity<ArticleListResponse> getAllReplies(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam BoardType boardType,
        @RequestParam String sort
    ) {
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
}
