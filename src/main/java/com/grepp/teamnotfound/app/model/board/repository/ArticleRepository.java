package com.grepp.teamnotfound.app.model.board.repository;

import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import com.grepp.teamnotfound.app.model.board.entity.Article;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    Page<ArticleListDto> findByBoard(BoardType boardType, PageRequest pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Article a SET a.views = a.views + 1 WHERE a.articleId = :articleId AND a.deletedAt IS NULL")
    Integer plusViewById(@Param("articleId") Long articleId);


    @Query("SELECT COUNT(a) FROM Article  a WHERE a.createdAt BETWEEN :start AND :end")
    int countArticlesBetween(OffsetDateTime start, OffsetDateTime end);
}
