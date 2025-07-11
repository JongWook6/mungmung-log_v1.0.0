package com.grepp.teamnotfound.app.controller.api.article;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleListRequest;
import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleListResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleRequest;
import com.grepp.teamnotfound.app.controller.api.article.payload.LikeResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.PageInfo;
import com.grepp.teamnotfound.app.model.board.ArticleService;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.code.BoardErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/community/articles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleApiController {

    private final ArticleService articleService;

    @GetMapping("/v1")
    @Operation(summary = "특정 게시판의 게시글 리스트 조회")
    public ResponseEntity<ArticleListResponse> getAllArticles(
        @ModelAttribute ArticleListRequest request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(BoardErrorCode.BOARD_INVALID_PAGE);
        }

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

        PageInfo pageInfo = PageInfo.builder()
            .page(request.getPage())
            .size(request.getSize())
            .totalPages(5)
            .totalElements(48)
            .hasNext(true)
            .hasPrevious(false)
            .build();

        response.setPageInfo(pageInfo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v2")
    @Operation(summary = "특정 게시판의 게시글 리스트 조회")
    public ResponseEntity<ArticleListResponse> getAllArticlesV2(
        @ModelAttribute ArticleListRequest request,
        BindingResult bindingResult
    ) {
        // TODO 실제 로직으로 구현 예정
        if (bindingResult.hasErrors()) {
            throw new BusinessException(BoardErrorCode.BOARD_INVALID_PAGE);
        }

        PageRequest pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<ArticleListDto> page = articleService.findPaged(pageable);

        if (request.getPage() != 1 && page.getContent().isEmpty()) {
            throw new BusinessException(BoardErrorCode.BOARD_INVALID_PAGE);
        }

        ArticleListResponse response = new ArticleListResponse();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/v1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "새로운 게시글 작성")
    public ResponseEntity<?> createArticle(
        @RequestPart("request") ArticleRequest request,
        @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.ok(Map.of("data", Map.of("articleId", 1, "msg", "게시글이 정상적으로 등록되었습니다.")));
    }

    @GetMapping("/v1/{articleId}")
    @Operation(summary = "게시글 상세 조회")
    public ResponseEntity<?> getArticle(
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
        return ResponseEntity.ok(Map.of("data", response));
    }

    @PatchMapping( value = "/v1/{articleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 수정")
    public ResponseEntity<?> updateArticle(
        @PathVariable Long articleId,
        @RequestPart("request") ArticleRequest request,
        @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.ok(Map.of("data", Map.of("msg", "게시글이 정상적으로 수정되었습니다.")));
    }

    @DeleteMapping("/v1/{articleId}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<?> deleteArticle(
        @PathVariable Long articleId
    ) {
        return ResponseEntity.ok(Map.of("data", Map.of("msg", "게시글이 정상적으로 삭제되었습니다.")));
    }

    @PostMapping("/v1/{articleId}/like")
    @Operation(summary = "게시글 좋아요 요청")
    public ResponseEntity<?> likeArticle(
        @PathVariable Long articleId
    ) {
        LikeResponse response = new LikeResponse();
        response.setLike(15);
        response.setIsLiked(true);
        return ResponseEntity.ok(Map.of("data", response));
    }

    @DeleteMapping("/v1/{articleId}/like")
    @Operation(summary = "게시글 좋아요 취소")
    public ResponseEntity<?> undoLikeArticle(
        @PathVariable Long articleId
    ) {
        LikeResponse response = new LikeResponse();
        response.setLike(14);
        response.setIsLiked(false);
        return ResponseEntity.ok(Map.of("data", response));
    }

    @GetMapping("/v1/{articleId}/reply")
    @Operation(summary = "게시글 댓글 개수")
    public ResponseEntity<?> getReplyCount(
        @PathVariable Long articleId
    ) {
        return ResponseEntity.ok(Map.of("data", Map.of("replies", 15)));
    }

    @GetMapping("/v1/{articleId}/like")
    @Operation(summary = "게시글 좋아요 개수")
    public ResponseEntity<?> getLikeCount(
        @PathVariable Long articleId
    ) {
        return ResponseEntity.ok(Map.of("data", Map.of("likes", 20)));
    }
}
