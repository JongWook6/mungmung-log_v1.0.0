package com.grepp.teamnotfound.app.model.board;

import com.grepp.teamnotfound.app.model.board.entity.ArticleLike;
import com.grepp.teamnotfound.app.model.board.repository.ArticleLikeRepository;
import com.grepp.teamnotfound.app.model.board.repository.ArticleRepository;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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
//    @Scheduled(fixedDelay = 300000)
//    @Transactional
    public void processLikeBatch() {
        log.info("Processing likes batch...");

        Set<Long> changedArticleIds = redisLikeService.getAllChangedArticleIdsAndClear();

        if (changedArticleIds.isEmpty()) {
            log.info("No changed article found. Skipping batch processing.");
            return;
        }

        for (Long articleId : changedArticleIds) {
            Set<Object> likeRequests = redisLikeService.getAllLikeRequestsAndClear(articleId);
            Set<Object> unlikeRequests = redisLikeService.getAllUnlikeRequestsAndClear(articleId);

            // 좋아요 요청 처리
            if (likeRequests != null && !likeRequests.isEmpty()) {
                List<Long> usersToLike = likeRequests.stream()
                    .map(object -> Long.valueOf(object.toString()))
                    .toList();

                for (Long userId : usersToLike) {
                    // DB 에 해당 좋아요가 없는 경우만 추가
                    if (!articleLikeRepository.existsByArticle_ArticleIdAndUser_UserId(articleId, userId)) {
                        articleLikeRepository.save(
                            // getReferenceById 는 단순한 관계설정을 위한 엔티티 호출(DB에 접근하지 않음)
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
        }

        log.info("Likes batch processing finished for {} articles.", changedArticleIds.size());
    }

    // 1분마다 실행
    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void processLikeBatch2() {
        log.info("Processing likes batch...");

        Set<Long> changedArticleIds = redisLikeService.getAllChangedArticleIdsAndClear();

        if (changedArticleIds.isEmpty()) {
            log.info("No changed article found. Skipping batch processing.");
            return;
        }

        for (Long articleId : changedArticleIds) {
            Set<Object> likeRequests = redisLikeService.getAllLikeRequestsAndClear(articleId);
            Set<Object> unlikeRequests = redisLikeService.getAllUnlikeRequestsAndClear(articleId);

            List<Long> usersToLike = likeRequests.stream()
                .map(object -> Long.valueOf(object.toString()))
                .toList();

            List<Long> usersToUnlike = unlikeRequests.stream()
                .map(object -> Long.valueOf(object.toString()))
                .toList();

            // 좋아요를 한꺼번에 INSERT
            // NOTE 단순 반복문을 사용하면 너무 잦은 I/O 로 Redis 를 도입한 장점이 사라짐
            if (!usersToLike.isEmpty()) {
                List<Long> alreadyLiked = articleLikeRepository.findUserIdsByArticleId(articleId);

                List<ArticleLike> likesToInsert = usersToLike.stream()
                    .filter(userId -> !alreadyLiked.contains(userId))
                    .map(userId -> ArticleLike.builder()
                        .article(articleRepository.getReferenceById(articleId))
                        .user(userRepository.getReferenceById(userId))
                        .createdAt(OffsetDateTime.now())
                        .build()
                    ).toList();

                articleLikeRepository.saveAll(likesToInsert);
            }

            // 좋아요를 한꺼번에 DELETE
            if (!usersToUnlike.isEmpty()) {
                // NOTE 나중에 성능이 안나온다면 JPQL 쿼리로 직접 DELETE 쿼리 날려보기
                List<ArticleLike> likesToDelete = articleLikeRepository.findAllByArticleIdAndUserIds(articleId, usersToUnlike);
                articleLikeRepository.deleteAllInBatch(likesToDelete);
            }
        }

        log.info("Likes batch processing finished for {} articles.", changedArticleIds.size());
    }
}
