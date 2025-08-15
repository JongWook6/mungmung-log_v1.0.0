package com.grepp.teamnotfound.app.model.reply;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisReplyService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ARTICLE_REPLY_COUNT_KEY = "article:reply_count:";

    public Long getArticleReplyCount(Long articleId) {
        try {
            String key = ARTICLE_REPLY_COUNT_KEY + articleId;
            Object count = redisTemplate.opsForValue().get(key);
            if (count instanceof Integer) {
                return ((Integer) count).longValue();
            }
            if (count instanceof Long) {
                return (Long) count;
            }
        } catch (Exception e) {
            log.warn("[Redis fallback] getArticleReplyCount failed - articleId: {}", articleId, e);
        }
        return null;
    }

    public void setArticleReplyCount(Long articleId, Long count) {
        String key = ARTICLE_REPLY_COUNT_KEY + articleId;
        redisTemplate.opsForValue().set(key, count);
        redisTemplate.expire(key, 3, TimeUnit.DAYS);
    }

    public void incrementArticleReplyCount(Long articleId) {
        String key = ARTICLE_REPLY_COUNT_KEY + articleId;
        redisTemplate.opsForValue().increment(key, 1);
    }

    public void decrementArticleReplyCount(Long articleId) {
        String key = ARTICLE_REPLY_COUNT_KEY + articleId;
        redisTemplate.opsForValue().increment(key, -1);
    }
}
