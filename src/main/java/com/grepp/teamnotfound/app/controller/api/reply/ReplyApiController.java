package com.grepp.teamnotfound.app.controller.api.reply;

import com.grepp.teamnotfound.app.controller.api.reply.payload.ReplyListResponse;
import com.grepp.teamnotfound.app.controller.api.reply.payload.ReplyRequest;
import com.grepp.teamnotfound.app.model.reply.dto.ReplyListDto;
import io.swagger.v3.oas.annotations.Operation;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/community/articles/{articleId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReplyApiController {

    @GetMapping("/replies")
    @Operation(summary = "특정 게시글의 댓글 리스트 조회")
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

    @PostMapping("/replies")
    @Operation(summary = "댓글 작성")
    public ResponseEntity<?> createReply(
        @PathVariable int articleId,
        @ModelAttribute ReplyRequest request
    ) {
        return ResponseEntity.ok(Map.of("data", Map.of( "replyId", 5, "msg", "댓글이 정상적으로 등록되었습니다.")));
    }

    @PatchMapping("/replies/{replyId}")
    @Operation(summary = "댓글 수정")
    public ResponseEntity<?> updateReply(
        @PathVariable Long articleId,
        @PathVariable Long replyId,
        @ModelAttribute ReplyRequest request

    ) {
        return ResponseEntity.ok(Map.of("data", Map.of("msg", "댓글이 정상적으로 수정되었습니다.")));
    }

    @DeleteMapping("/replies/{replyId}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<?> deleteReply(
        @PathVariable Long articleId,
        @PathVariable Long replyId
    ) {
        return ResponseEntity.ok(Map.of("data", Map.of("msg", "댓글이 정상적으로 삭제되었습니다.")));
    }
}
