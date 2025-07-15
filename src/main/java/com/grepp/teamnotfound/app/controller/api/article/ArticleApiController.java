package com.grepp.teamnotfound.app.controller.api.article;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleListRequest;
import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleListResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleRequest;
import com.grepp.teamnotfound.app.controller.api.article.payload.LikeResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.PageInfo;
import com.grepp.teamnotfound.app.model.auth.domain.Principal;
import com.grepp.teamnotfound.app.model.board.ArticleService;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import com.grepp.teamnotfound.app.model.user.entity.UserDetailsImpl;
import com.grepp.teamnotfound.infra.error.exception.AuthException;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.code.AuthErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.BoardErrorCode;
import com.grepp.teamnotfound.infra.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
        // TODO BindingResult 로 세밀한 예외처리로 디벨롭
        if (request.getPage() < 1) {
            throw new BusinessException(BoardErrorCode.BOARD_INVALID_PAGE);
        }

        ArticleListResponse response = new ArticleListResponse();

        if (request.getPage() == 1) {
            for (int i = 1; i <= 10; i++) {
                ArticleListDto dto = ArticleListDto.builder()
                    .articleId(Integer.toUnsignedLong(i))
                    .title("마음이의 산책 일상 " + i)
                    .nickname("사용자 " + i)
                    .likes(i)
                    .replies(i)
                    .views(i)
                    .date(OffsetDateTime.now())
                    .build();

                response.getData().add(dto);
            }
        } else {
            for (int i = 1; i <= 9; i++) {
                ArticleListDto dto = ArticleListDto.builder()
                    .articleId(Integer.toUnsignedLong(i))
                    .title("마음이의 산책 일상 " + i)
                    .nickname("사용자 " + i)
                    .likes(i)
                    .replies(i)
                    .views(i)
                    .date(OffsetDateTime.now())
                    .build();

                response.getData().add(dto);
            }
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
        @ModelAttribute @Valid ArticleListRequest request
    ) {
        // NOTE 여기서 예외처리를 어떻게 하는 게 좋을까? -> GlobalExceptionHandler 에서 일괄 처리
        PageRequest pageable = PageRequest.of(
            request.getPage() - 1,
            request.getSize(),
            request.getSortType().toSort()
        );

        Page<ArticleListDto> page = articleService.searchArticles(
            request.getBoardType(),
            request.getSearchType(),
            request.getKeyword(),
            pageable
        );

        PageInfo pageInfo = PageInfo.builder()
            .page(page.getNumber() + 1)
            .size(page.getSize())
            .totalPages(page.getTotalPages())
            .totalElements((int) page.getTotalElements())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .build();

        return ResponseEntity.ok(new ArticleListResponse(page.getContent(), pageInfo));
    }

    @PostMapping(value = "/v1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "새로운 게시글 작성")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createArticle(
        @RequestPart("request") ArticleRequest request,
        @RequestPart(value = "images", required = false) List<MultipartFile> images,
        @AuthenticationPrincipal Principal principal
    ) {
        Long articleId = articleService.writeArticle(request, images, principal.getUserId());

        return ResponseEntity.ok(ApiResponse.success(Map.of("articleId", articleId)));
    }

    @GetMapping("/v1/{articleId}")
    @Operation(summary = "게시글 상세 조회")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getArticle(
        @PathVariable Long articleId,
        @AuthenticationPrincipal Principal principal
    ) {
        ArticleDetailResponse response = articleService.findByArticleIdAndUserId(articleId, principal.getUserId());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping( value = "/v1/{articleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 수정")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateArticle(
        @PathVariable Long articleId,
        @RequestPart("request") ArticleRequest request,
        @RequestPart(value = "images", required = false) List<MultipartFile> images,
        @AuthenticationPrincipal Principal principal
    ) {
        articleService.updateArticle(articleId, request, images, principal.getUserId());

        return ResponseEntity.ok(ApiResponse.success(Map.of("result", "게시글이 정상적으로 수정되었습니다.")));
    }

    @DeleteMapping("/v1/{articleId}")
    @Operation(summary = "게시글 삭제")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteArticle(
        @PathVariable Long articleId,
        @AuthenticationPrincipal Principal principal
    ) {
        articleService.deleteArticle(articleId, principal.getUserId());

        return ResponseEntity.ok(ApiResponse.success(Map.of("msg", "게시글이 정상적으로 삭제되었습니다.")));
    }

    @PostMapping("/v1/{articleId}/like")
    @Operation(summary = "게시글 좋아요 요청")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> likeArticle(
        @PathVariable Long articleId,
        @AuthenticationPrincipal Principal principal
    ) {
        // 사용자 이메일로 요청 회원 특정
        String userEmail = principal.getUsername();
        System.out.println("userEmail: " + userEmail);

        LikeResponse response = LikeResponse.builder()
            .articleId(13L)
            .userEmail("user111@email.com")
            .like(15)
            .isLiked(true)
            .build();

        return ResponseEntity.ok(Map.of("data", response));
    }

    @DeleteMapping("/v1/{articleId}/like")
    @Operation(summary = "게시글 좋아요 취소")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> undoLikeArticle(
        @PathVariable Long articleId,
        @AuthenticationPrincipal Principal principal
    ) {

        LikeResponse response = LikeResponse.builder()
            .articleId(13L)
            .userEmail("user111@email.com")
            .like(14)
            .isLiked(false)
            .build();

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
