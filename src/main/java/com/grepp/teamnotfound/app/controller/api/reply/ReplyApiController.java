package com.grepp.teamnotfound.app.controller.api.reply;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleListResponse;
import com.grepp.teamnotfound.app.controller.api.reply.payload.ReplyListResponse;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import com.grepp.teamnotfound.app.model.reply.dto.ReplyListDto;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/community/articles/{articleId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReplyApiController {

    // 댓글 리스트
    @GetMapping("/replies")
    public ResponseEntity<ReplyListResponse> getAllReplies(
        @PathVariable int articleId
    ) {
        ReplyListResponse response = new ReplyListResponse();

        for (int i = 1; i <= 3; i++) {
            ReplyListDto dto = ReplyListDto.builder()
                .replyId(Integer.toUnsignedLong(i))
                .nickname("유저 " + i)
                .content(i + "번째 댓글입니다.")
                .date(OffsetDateTime.now())
                .build();

            response.getData().add(dto);
        }
        return ResponseEntity.ok(response);
    }

    // 댓글 작성
    @PostMapping("/replies")
    public ResponseEntity<?> createReply(
        @PathVariable int articleId
    ) {
        return ResponseEntity.ok("댓글이 정상적으로 생성되었습니다.");
    }

    // 댓글 수정
    @PatchMapping("/replies/{replyId}")
    public ResponseEntity<?> updateReply(
        @PathVariable Long articleId,
        @PathVariable Long replyId
    ) {
        return ResponseEntity.ok("댓글이 정상적으로 수정되었습니다.");
    }

    // 댓글 삭제
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<?> deleteReply(
        @PathVariable Long articleId,
        @PathVariable Long replyId
    ) {
        return ResponseEntity.ok("댓글이 정상적으로 삭제되었습니다.");
    }


}
