package com.grepp.teamnotfound.app.model.reply.repository;

import com.grepp.teamnotfound.app.model.reply.entity.Reply;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Reply r SET r.deletedAt = :deletedAt WHERE r.article.articleId = :articleId AND r.deletedAt IS NULL")
    void softDeleteByArticleId(@Param("articleId") Long articleId, @Param("deletedAt") OffsetDateTime deletedAt);

    Integer countByArticle_ArticleId(Long articleId);

    Page<Reply> findByArticle_ArticleIdAndDeletedAtIsNullAndReportedAtIsNull(Long articleId, Pageable pageable);
}
