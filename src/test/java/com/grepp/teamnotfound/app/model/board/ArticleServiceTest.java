package com.grepp.teamnotfound.app.model.board;

import static org.junit.jupiter.api.Assertions.*;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleRequest;
import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.entity.Article;
import com.grepp.teamnotfound.app.model.board.entity.Board;
import com.grepp.teamnotfound.app.model.board.repository.ArticleImgRepository;
import com.grepp.teamnotfound.app.model.board.repository.ArticleRepository;
import com.grepp.teamnotfound.app.model.board.repository.BoardRepository;
import com.grepp.teamnotfound.app.model.reply.repository.ReplyRepository;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.error.exception.BoardException;
import com.grepp.teamnotfound.infra.error.exception.code.BoardErrorCode;
import com.grepp.teamnotfound.infra.util.file.FileDto;
import com.grepp.teamnotfound.infra.util.file.GoogleStorageManager;
import java.io.IOException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
class ArticleServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary // 실제 말고 fake 가 등록되도록
        public GoogleStorageManager fakeGoogleStorageManager() {
            return new FakeGoogleStorageManager();
        }
    }

    static class FakeGoogleStorageManager extends GoogleStorageManager {

        @Override
        protected void uploadFile(MultipartFile file, FileDto fileDto) throws IOException {
            System.out.println("이미지가 버킷에 업로드되었습니다.");
        }
    }

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleImgRepository articleImgRepository;
    @Autowired
    private ReplyRepository replyRepository;

    private Long testUserId;
    private Long testBoardId;

    @BeforeEach
    void setUp() {
        User user = User.builder()
            .email("test@example.com")
            .name("신짱구")
            .nickname("액션가면")
            .password("123qwe!@#")
            .role(Role.ROLE_USER)
            .build();
        userRepository.save(user);
        this.testUserId = user.getUserId();

        Board board = Board.builder()
            .name("FREE")
            .build();
        boardRepository.save(board);
        this.testBoardId = board.getBoardId();
    }

    @Test
    @DisplayName("새로운 게시글 작성")
    void writeArticle() {
        // given
        ArticleRequest request = ArticleRequest.builder()
            .title("title")
            .content("content")
            .boardType(BoardType.QUESTION)
            .build();

        List<MultipartFile> images = List.of(); // 실제 파일 없이 빈 리스트

        // when
        Long savedArticleId = articleService.writeArticle(request, images, testUserId);

        // then
        Article saved = articleRepository.findById(savedArticleId).orElseThrow();
        assertEquals("title", saved.getTitle());
        assertEquals("content", saved.getContent());
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void findByArticleIdAndUserId() {
        // given
        ArticleRequest request = ArticleRequest.builder()
            .title("title1")
            .content("content1")
            .boardType(BoardType.QUESTION)
            .build();

        List<MultipartFile> images = List.of(); // 실제 파일 없이 빈 리스트
        Long savedArticleId = articleService.writeArticle(request, images, testUserId);

        // when
        ArticleDetailResponse response = articleService.findByArticleIdAndUserId(
            savedArticleId, testUserId);

        // then
        assertEquals("title1", response.getTitle());
        assertEquals("content1", response.getContent());
    }

    @Test
    @DisplayName("게시글 수정")
    void updateArticle() {
        // given
        ArticleRequest oldReq = ArticleRequest.builder()
            .title("old title")
            .content("old content")
            .boardType(BoardType.QUESTION)
            .build();
        Long savedArticleId = articleService.writeArticle(oldReq, List.of(), testUserId);
        ArticleRequest newReq = ArticleRequest.builder()
            .title("new title")
            .content("new content")
            .boardType(BoardType.QUESTION)
            .build();

        // when
        articleService.updateArticle(savedArticleId, newReq, List.of(), testUserId);

        // then
        Article updatedArticle = articleRepository.findById(savedArticleId).orElseThrow();
        assertEquals("new title", updatedArticle.getTitle());
        assertEquals("new content", updatedArticle.getContent());
    }

    @Test
    @DisplayName("타 회원의 게시글 수정 불가")
    void updateArticleForbidden() {
        // given
        ArticleRequest oldReq = ArticleRequest.builder()
            .title("old title")
            .content("old content")
            .boardType(BoardType.QUESTION)
            .build();
        Long savedArticleId = articleService.writeArticle(oldReq, List.of(), testUserId);
        ArticleRequest newReq = ArticleRequest.builder()
            .title("new title")
            .content("new content")
            .boardType(BoardType.QUESTION)
            .build();

        // then
        Assertions.assertThatThrownBy(
                () -> articleService.updateArticle(savedArticleId, newReq, List.of(), 1L))
            .isInstanceOf(BoardException.class);
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteArticle() {
        // given
        ArticleRequest request = ArticleRequest.builder()
            .title("title for delete")
            .content("content for delete")
            .boardType(BoardType.QUESTION)
            .build();
        Long savedArticleId = articleService.writeArticle(request, List.of(), testUserId);

        // when
        articleService.deleteArticle(savedArticleId, testUserId);

        // then
        Assertions.assertThatThrownBy(
                () -> articleService.findByArticleIdAndUserId(savedArticleId, testUserId))
            .isInstanceOf(BoardException.class);
    }

    @Test
    @DisplayName("타 회원의 게시글 삭제 불가")
    void deleteArticleForbidden() {
        // given
        ArticleRequest request = ArticleRequest.builder()
            .title("title for delete")
            .content("content for delete")
            .boardType(BoardType.QUESTION)
            .build();
        Long savedArticleId = articleService.writeArticle(request, List.of(), testUserId);

        // then
        Assertions.assertThatThrownBy(
                () -> articleService.deleteArticle(savedArticleId, 1L))
            .isInstanceOf(BoardException.class);
    }
}