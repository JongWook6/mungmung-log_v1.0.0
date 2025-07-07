package com.grepp.teamnotfound.app.model.board.repository;

import com.grepp.teamnotfound.app.model.board.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
