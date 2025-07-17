package com.grepp.teamnotfound.app.model.reply.repository;

import com.grepp.teamnotfound.app.model.reply.entity.Reply;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Reply r SET r.deletedAt = :deletedAt WHERE r.article.articleId = :articleId AND r.deletedAt IS NULL")
    void softDeleteByArticleId(@Param("articleId") Long articleId, @Param("deletedAt") OffsetDateTime deletedAt);


    Optional<Long> findArticleIdByReplyId(Long replyId);
}
