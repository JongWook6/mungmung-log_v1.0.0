package com.grepp.teamnotfound.app.model.board;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLikeService {
    // TODO Redis 가 다운되었을 경우에 대한 대비 필요

    private final RedisTemplate<String, Object> redisTemplate;

    // 배치 대상 article
    private static final String BATCH_ARTICLE_IDS_KEY = "batch:article:ids";

    public void addLikeRequest(Long articleId, Long userId) {
        String likeKey = "article:like:" + articleId;
        String unlikeKey = "article:unlike:" + articleId;

        // 반대 요청이 이미 존재하면 제거하고 현재 요청은 무시
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(unlikeKey, userId))) {
            redisTemplate.opsForSet().remove(unlikeKey, userId);
            redisTemplate.opsForSet().remove(BATCH_ARTICLE_IDS_KEY, articleId);
            return;
        }
        redisTemplate.opsForSet().add(likeKey,userId);
        redisTemplate.expire(likeKey, 5, TimeUnit.MINUTES); // 5분 후 만료

        // 요청 받은 article id 저장
        redisTemplate.opsForSet().add(BATCH_ARTICLE_IDS_KEY, articleId);
    }

    public void addUnlikeRequest(Long articleId, Long userId) {
        String likeKey = "article:like:" + articleId;
        String unlikeKey = "article:unlike:" + articleId;

        // 반대 요청이 이미 존재하면 제거하고 현재 요청은 무시
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeKey, userId))) {
            redisTemplate.opsForSet().remove(likeKey, userId);
            redisTemplate.opsForSet().remove(BATCH_ARTICLE_IDS_KEY, articleId);
            return;
        }

        redisTemplate.opsForSet().add(unlikeKey, userId);
        redisTemplate.expire(unlikeKey, 5, TimeUnit.MINUTES); // 5분 후 만료

        // 요청 받은 article id 저장
        redisTemplate.opsForSet().add(BATCH_ARTICLE_IDS_KEY, articleId);
    }

    // 사용자별 특정 게시글 좋아요 상태를 Redis 에 저장
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

    // 사용자가 특정 게시글을 좋아요 했는지 Redis 에서 확인
    public boolean isUserLikedInRedis(Long articleId, Long userId) {
        String userLikeStatusKey = "user:liked_status:" + userId + ":" + articleId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(userLikeStatusKey));
    }

    // 특정 게시글의 좋아요 요청 유저 ID 목록 (Batch 처리용)
    public Set<Object> getLikeRequests(Long articleId) {
        String key = "article:like:" + articleId;
        return redisTemplate.opsForSet().members(key);
    }

    // 특정 게시글의 좋아요 취소 요청 유저 ID 목록 (Batch 처리용)
    public Set<Object> getUnlikeRequests(Long articleId) {
        String key = "article:unlike:" + articleId;
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * Batch 처리용
     * 트랜잭션으로 관리하기 위해 SessionCallback 사용
     * */

    // 좋아요/취소 요청을 받은 articleId 반환
    public Set<Long> getAllChangedArticleIdsAndClear() {
        String sourceKey = BATCH_ARTICLE_IDS_KEY;

        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();

                // Set 의 모든 멤버를 가져옴
                Set<Object> members = ((RedisTemplate<String, Object>) operations)
                    .opsForSet()
                    .members(sourceKey);

                // Set 을 삭제
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
        String sourceKey = "article:like:" + articleId;

        // Redis 트랜잭션을 사용하여 (요청 조회 + 삭제)를 원자적으로 처리
        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {

                operations.multi(); // 트랜잭션 시작

                // 현재 Set 의 모든 멤버를 가져옴
                Set<Object> members = ((RedisTemplate<String, Object>)operations)
                    .opsForSet()
                    .members(sourceKey); // Set 반환

                // 현재 Set 을 삭제
                ((RedisTemplate<String, Object>)operations).delete(sourceKey); // 삭제된 키의 개수 반환

                return operations.exec(); // 트랜잭션 내의 각 명령을 순서대로 실행
            }
        });

        // results 리스트 구조 = [Set<Object>, Long]
        if (results != null && !results.isEmpty() && results.getFirst() instanceof Set) {
            return (Set<Object>) results.getFirst();
        }
        return Collections.emptySet();
    }

    public Set<Object> getAllUnlikeRequestsAndClear(Long articleId) {
        String sourceKey = "article:unlike:" + articleId;

        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {

                operations.multi(); // 트랜잭션 시작

                Set<Object> members = ((RedisTemplate<String, Object>) operations)
                    .opsForSet()
                    .members(sourceKey); // Set 반환

                ((RedisTemplate<String, Object>) operations).delete(sourceKey); // 삭제된 키의 개수 반환

                return operations.exec(); // 트랜잭션 내의 각 명령을 순서대로 실행
            }
        });

        // results 리스트 = [Set<Object>, Long]
        if (results != null && !results.isEmpty() && results.getFirst() instanceof Set) {
            return (Set<Object>) results.getFirst();
        }
        return Collections.emptySet();
    }
}
