package com.grepp.teamnotfound.app.model.board;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLikeService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void addLikeRequest(Long articleId, Long userId) {
        String key = "article:like:" + articleId;
        redisTemplate.opsForSet().add(key,userId);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES); // 5분 후 만료
    }

    public void addUnlikeRequest(Long articleId, Long userId) {
        String key = "article:unlike:" + articleId;
        redisTemplate.opsForSet().add(key, userId);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES); // 5분 후 만료
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
    public boolean isUserLiked(Long articleId, Long userId) {
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

    // Redis 에 저장된 좋아요/취소 요청 데이터 지우기
    public void clearLikeRequests(Long articleId) {
        redisTemplate.delete("article:like:" + articleId);
        redisTemplate.delete("article:unlike:" + articleId);
    }

    /**
     *  좋아요/취소 요청이 충돌했을 때 Redis Set 에서 서로를 제거하는 메서드
     * */
    public void removeUnlikeRequestIfExist(Long articleId, Long userId) {
        String unlikeKey = "article:unlike:" + articleId;
        redisTemplate.opsForSet().remove(unlikeKey, userId);
    }

    public void removeLikeRequestIfExist(Long articleId, Long userId) {
        String likeKey = "article:like:" + articleId;
        redisTemplate.opsForSet().remove(likeKey, userId);
    }
}
