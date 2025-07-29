package com.grepp.teamnotfound.app.model.board;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLikeService {
    // TODO Redis 가 다운되었을 경우에 대한 대비 필요

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String BATCH_ARTICLE_IDS_KEY = "batch:article:ids";
    private static final String ARTICLE_LIKES_TOTAL_COUNT_KEY = "article:likes_total_count:";
    private static final String ARTICLE_LIKE_KEY = "article:like:";
    private static final String ARTICLE_UNLIKE_KEY = "article:unlike:";

    public void addLikeRequest(Long articleId, Long userId) {
        String likeKey = ARTICLE_LIKE_KEY + articleId;
        String unlikeKey = ARTICLE_UNLIKE_KEY + articleId;

        // 반대 요청이 이미 존재하면 제거하고 현재 요청은 무시
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(unlikeKey, userId))) {
            redisTemplate.opsForSet().remove(unlikeKey, userId);
            redisTemplate.opsForSet().remove(BATCH_ARTICLE_IDS_KEY, articleId);
            return;
        }
        redisTemplate.opsForSet().add(likeKey,userId);
        redisTemplate.expire(likeKey, 5, TimeUnit.MINUTES);

        redisTemplate.opsForSet().add(BATCH_ARTICLE_IDS_KEY, articleId);
    }

    public void addUnlikeRequest(Long articleId, Long userId) {
        String likeKey = ARTICLE_LIKE_KEY + articleId;
        String unlikeKey = ARTICLE_UNLIKE_KEY + articleId;

        // 반대 요청이 이미 존재하면 제거하고 현재 요청은 무시
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeKey, userId))) {
            redisTemplate.opsForSet().remove(likeKey, userId);
            redisTemplate.opsForSet().remove(BATCH_ARTICLE_IDS_KEY, articleId);
            return;
        }

        redisTemplate.opsForSet().add(unlikeKey, userId);
        redisTemplate.expire(unlikeKey, 5, TimeUnit.MINUTES);

        redisTemplate.opsForSet().add(BATCH_ARTICLE_IDS_KEY, articleId);
    }

    // 사용자의 좋아요 상태를 Redis 에 저장
    // 배치처리 전 DB에 다녀오지 않기 위함
    public void setUserLikedStatus(Long articleId, Long userId, boolean liked) {
        String userLikedKey = "user:liked_status:" + userId + ":" + articleId;
        if (liked) {
            redisTemplate.opsForValue().set(userLikedKey, "1");
            redisTemplate.expire(userLikedKey, 7, TimeUnit.DAYS);
        } else {
            redisTemplate.delete(userLikedKey);
        }
    }

    // 사용자의 좋아요 상태를 Redis 에서 확인
    public boolean isUserLikedInRedis(Long articleId, Long userId) {
        try {
            String userLikeStatusKey = "user:liked_status:" + userId + ":" + articleId;
            return Boolean.TRUE.equals(redisTemplate.hasKey(userLikeStatusKey));
        } catch (Exception e) {
            log.warn("[Redis fallback] isUserLikedInRedis fail - articleId: {}, userId: {}", articleId, userId, e);
            return false;
        }
    }


    /**
     * Batch 처리용
     * (요청조회 + 요청삭제)를 하나의 트랜잭션으로 관리하기 위해 SessionCallback 사용
     * */

    // 좋아요/취소 요청을 받은 articleId 반환
    public Set<Long> getAllChangedArticleIdsAndClear() {
        String sourceKey = BATCH_ARTICLE_IDS_KEY;

        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                Set<Object> members = ((RedisTemplate<String, Object>) operations).opsForSet().members(sourceKey);
                ((RedisTemplate<String, Object>) operations).delete(sourceKey);
                return operations.exec();
            }
        });

        if (results != null && !results.isEmpty() && results.getFirst() instanceof Set) {
            return ((Set<Object>) results.getFirst()).stream()
                .map(object -> Long.valueOf(object.toString()))
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public Set<Object> getAllLikeRequestsAndClear(Long articleId) {
        return getSetAndClear(ARTICLE_LIKE_KEY + articleId, "getAllLikeRequestsAndClear");
    }

    public Set<Object> getAllUnlikeRequestsAndClear(Long articleId) {
        return getSetAndClear(ARTICLE_UNLIKE_KEY + articleId, "getAllUnlikeRequestsAndClear");
    }

    private Set<Object> getSetAndClear(String key, String logContext) {
        try {
            List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
                @Override
                public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                    // 트랜잭션 시작
                    operations.multi();
                    // Set 반환
                    Set<Object> members = ((RedisTemplate<String, Object>) operations).opsForSet().members(key);
                    // 삭제된 키의 개수 반환
                    ((RedisTemplate<String, Object>) operations).delete(key);
                    // 트랜잭션 내의 각 명령을 순서대로 실행
                    return operations.exec();
                }
            });

            // results 리스트 = [Set<Object>, Long]
            if (results != null && !results.isEmpty() && results.getFirst() instanceof Set) {
                return (Set<Object>) results.getFirst();
            }
        } catch (Exception e) {
            log.error("[Redis fallback] {} fail - key = {}", logContext, key, e);
        }
        return Collections.emptySet();
    }

    /**
     * 게시글별 좋아요 수 캐시용
     * 좋아요/좋아요 취소 요청 시 최종 좋아요 수를 계산하기 위한 IO를 줄이기 위함
     * */

    // Redis 에서 좋아요 카운트 가져오기
    public Long getArticleLikesCount(Long articleId) {
        try {
            String key = ARTICLE_LIKES_TOTAL_COUNT_KEY + articleId;
            Object count = redisTemplate.opsForValue().get(key);
            if (count instanceof Integer) return ((Integer) count).longValue();
            if (count instanceof Long) return (Long) count;
        } catch (Exception e) {
            log.warn("[Redis fallback] getArticleLikesCount fail - articleId: {}", articleId, e);
        }
        return null;
    }

    // Redis 에 좋아요 카운트 설정
    public void setArticleLikesCount(Long articleId, Long count) {
        String key = ARTICLE_LIKES_TOTAL_COUNT_KEY + articleId;
        redisTemplate.opsForValue().set(key, count);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
    }

    public void incrementArticleLikesCount(Long articleId) {
        String key = ARTICLE_LIKES_TOTAL_COUNT_KEY + articleId;
        redisTemplate.opsForValue().increment(key, 1);
    }

    public void decrementArticleLikesCount(Long articleId) {
        String key = ARTICLE_LIKES_TOTAL_COUNT_KEY + articleId;
        redisTemplate.opsForValue().increment(key, -1);
    }
}
