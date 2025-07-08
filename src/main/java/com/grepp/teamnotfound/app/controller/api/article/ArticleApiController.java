package com.grepp.teamnotfound.app.controller.api.article;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleListResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.Pagination;
import com.grepp.teamnotfound.app.model.board.ArticleService;
import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<ArticleListResponse> getAllArticles(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam BoardType boardType,
        @RequestParam String sort
    ) {
        ArticleListResponse response = new ArticleListResponse();

        for (int i = 1; i <= 5; i++) {
            ArticleListDto dto = ArticleListDto.builder()
                .articleId(Integer.toUnsignedLong(i))
                .title("마음이의 산책 일상 " + i)
                .nickname("사용자 " + i)
                .replies(i)
                .views(i)
                .date(OffsetDateTime.now())
                .build();

            response.getData().add(dto);
        }

        Pagination pagination = new Pagination();
        pagination.setTotal(45); // 전체 게시글 수
        pagination.setPage(page); // 요청 페이지
        pagination.setSize(size); // 요청 게시글 수
        pagination.setTotalPages((int) Math.ceil((double) pagination.getTotal() / pagination.getSize()));

        response.setPagination(pagination);
        return ResponseEntity.ok(response);
    }

    // 새로운 게시글 작성
    @PostMapping
    public ResponseEntity<?> createArticle() {
        return ResponseEntity.ok("게시글이 정상적으로 생성되었습니다.");
    }

    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDetailResponse> getArticle(
        @PathVariable Long articleId
    ) {
        ArticleDetailResponse response = new ArticleDetailResponse();
        response.setArticleId(articleId);
        response.setNickname("사용자 1");
        response.setProfileImgPath("/upload/profileImg");
        response.setDate(OffsetDateTime.now());
        response.setTitle("요즘 밥을 잘 안먹는데 왜 그럴까요?");
        response.setContent("우리집 댕댕이가 날씨 때문인지 최근 며칠 간 밥을 잘 안먹어서 고민입니다");
        response.setReplies(10);
        response.setLikes(15);
        response.setViews(20);
        response.setArticleImgPathList(List.of("/upload/img1", "/upload/img2", "/upload/img3"));
        return ResponseEntity.ok(response);
    }

    // 게시글 수정
    @PatchMapping("/{articleId}")
    public ResponseEntity<?> updateArticle(
        @PathVariable Long articleId
    ) {
        return ResponseEntity.ok("게시글이 정상적으로 수정되었습니다.");
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticle(
        @PathVariable Long articleId
    ) {
        return ResponseEntity.ok("게시글이 정상적으로 삭제되었습니다.");
    }

    // 게시글 좋아요 요청
    @PostMapping("/{articleId}/like")
    public ResponseEntity<?> likeArticle(
        @PathVariable Long articleId
    ) {
        return ResponseEntity.ok("좋아요 + 1");
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/{articleId}/like")
    public ResponseEntity<?> undoLikeArticle(
        @PathVariable Long articleId
    ) {
        return ResponseEntity.ok("좋아요 - 1");
    }

}
