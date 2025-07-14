package com.grepp.teamnotfound.app.model.board.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.infra.error.exception.BoardException;
import com.grepp.teamnotfound.infra.error.exception.code.BoardErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("프로필 이미지를 등록한 회원이 작성한 게시글")
    void searchArticleDetailWithProfileImg() {
        ArticleDetailResponse result = articleRepository.findDetailById(10008L, 1L);
        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("프로필 이미지를 등록했다가 삭제한 회원이 작성한 게시글")
    void searchArticleDetailWithoutProfileImg() {
        ArticleDetailResponse result = articleRepository.findDetailById(1L, 1L);
        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("삭제된 게시글")
    void searchArticleDetailDeleted() {
        ArticleDetailResponse result = articleRepository.findDetailById(3L, 1L);
        System.out.println("result = " + result);

        // 나중에 서비스 테스트 코드에 사용
//        Assertions.assertThatThrownBy(() -> articleRepository.findDetailById(3L, 1L))
//            .isInstanceOf(BoardException.class)
//            .hasMessageContaining(BoardErrorCode.ARTICLE_DELETED.getMessage());
    }
}