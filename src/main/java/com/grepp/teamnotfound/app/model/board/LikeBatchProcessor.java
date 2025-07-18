package com.grepp.teamnotfound.app.model.board;

import com.grepp.teamnotfound.app.model.board.entity.ArticleLike;
import com.grepp.teamnotfound.app.model.board.repository.ArticleLikeRepository;
import com.grepp.teamnotfound.app.model.board.repository.ArticleRepository;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeBatchProcessor {

    private final RedisLikeService redisLikeService;
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final UserRepository userRepository;

    // 1분마다 실행
//    @Scheduled(fixedDelay = 60000)
    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void processLikeBatch() {

        // NOTE 아니 모든 게시글을 가져온다고?
        List<Long> articleIds = articleRepository.findAllArticleIds();

        for (Long articleId : articleIds) {
            Set<Object> likeRequests = redisLikeService.getLikeRequests(articleId);
            Set<Object> unlikeRequests = redisLikeService.getUnlikeRequests(articleId);

            // 좋아요 요청 처리
            if (likeRequests != null && !likeRequests.isEmpty()) {
                List<Long> usersToLike = likeRequests.stream()
                    .map(object -> Long.valueOf(object.toString()))
                    .toList();

                for (Long userId : usersToLike) {
                    // DB 에 해당 좋아요가 없는 경우만 추가
                    if (!articleLikeRepository.existsByArticle_ArticleIdAndUser_UserId(articleId,
                        userId)) {
                        articleLikeRepository.save(
                            // getReferenceById() 는 단순한 관계설정을 위한 엔티티 호출
                            ArticleLike.builder()
                                .article(articleRepository.getReferenceById(articleId))
                                .user(userRepository.getReferenceById(userId))
                                .createdAt(OffsetDateTime.now())
                                .build()
                        );
                    }
                }
            }

            // 좋아요 취소 요청 처리
            if (unlikeRequests != null && !unlikeRequests.isEmpty()) {
                List<Long> usersToUnlike = unlikeRequests.stream()
                    .map(object -> Long.valueOf(object.toString()))
                    .toList();

                for (Long userId : usersToUnlike) {
                    // DB 에 해당 좋아요가 있는 경우만 삭제
                    articleLikeRepository
                        .findByArticle_ArticleIdAndUser_UserId(articleId, userId)
                        .ifPresent(articleLikeRepository::delete);
                }
            }

            // 처리 완료된 Redis 배치 요청 데이터 비우기
            redisLikeService.clearLikeRequests(articleId);
        }

        log.info("Likes batch processing finished");
    }
}
