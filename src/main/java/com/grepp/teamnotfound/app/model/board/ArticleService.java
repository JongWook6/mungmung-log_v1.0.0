package com.grepp.teamnotfound.app.model.board;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleRequest;
import com.grepp.teamnotfound.app.model.board.code.BoardType;
import com.grepp.teamnotfound.app.model.board.code.SearchType;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import com.grepp.teamnotfound.app.model.board.entity.Article;
import com.grepp.teamnotfound.app.model.board.entity.ArticleImg;
import com.grepp.teamnotfound.app.model.board.entity.Board;
import com.grepp.teamnotfound.app.model.board.repository.ArticleImgRepository;
import com.grepp.teamnotfound.app.model.board.repository.ArticleRepository;
import com.grepp.teamnotfound.app.model.board.repository.BoardRepository;
import com.grepp.teamnotfound.app.model.reply.repository.ReplyRepository;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.code.ImgType;
import com.grepp.teamnotfound.infra.error.exception.AuthException;
import com.grepp.teamnotfound.infra.error.exception.BoardException;
import com.grepp.teamnotfound.infra.error.exception.CommonException;
import com.grepp.teamnotfound.infra.error.exception.code.AuthErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.BoardErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.CommonErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import com.grepp.teamnotfound.infra.util.file.FileDto;
import com.grepp.teamnotfound.infra.util.file.GoogleStorageManager;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    private final ModelMapper modelMapper;
    private final GoogleStorageManager fileManager;
    private final ArticleImgRepository articleImgRepository;
    private final ReplyRepository replyRepository;

    public Page<ArticleListDto> findPaged(PageRequest pageable) {
//        List articleRepository.findPaged(pageable);
        return null;
    }

    public Page<ArticleListDto> searchArticles(BoardType boardType, SearchType searchType,
        String keyword, PageRequest pageable) {

        if (keyword == null || searchType == null) {
            return articleRepository.findByBoard(boardType, pageable);
        }

        return null;
    }

    @Transactional
    public Long writeArticle(ArticleRequest request, List<MultipartFile> images, String userEmail) {
        // 토큰 발급 시점과 현재 DB 사이의 무결성 검증
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new AuthException(UserErrorCode.USER_NOT_FOUND));

        Board board = boardRepository.findByName(request.getBoardType().name())
            .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        Article article = Article.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .views(0)
            .user(user)
            .board(board)
            .build();

        Article savedArticle = articleRepository.save(article);
        uploadAndSaveImgs(images, savedArticle);

        return savedArticle.getArticleId();
    }

    @Transactional(readOnly = true)
    public ArticleDetailResponse findByArticleIdAndEmail(Long articleId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new AuthException(UserErrorCode.USER_NOT_FOUND));;

        ArticleDetailResponse response = articleRepository.findDetailById(articleId, user.getUserId());

        if (response == null) {
            throw new BoardException(BoardErrorCode.ARTICLE_NOT_FOUND);
        }

        if (response.getIsDeleted()) {
            throw new BoardException(BoardErrorCode.ARTICLE_DELETED);
        }

        if (response.getIsReported()) {
            throw new BoardException(BoardErrorCode.ARTICLE_REPORTED);
        }

        return response;
    }

    @Transactional
    public void updateArticle(Long articleId, ArticleRequest request, List<MultipartFile> images,
        String userEmail) {
        // NOTE DB 에 너무 많이 접근하는 건가?
        User requestUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new AuthException(UserErrorCode.USER_NOT_FOUND));

        // NOTE 게시판 자체를 바꾸는 기능은 제공하지 않으니까 게시판 체크는 안해도 될 것 같은데
        Board board = boardRepository.findByName(request.getBoardType().name())
            .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        Article beforeArticle = articleRepository.findById(articleId)
            .orElseThrow(() -> new BoardException(BoardErrorCode.ARTICLE_NOT_FOUND));

        if (!beforeArticle.getUser().getUserId().equals(requestUser.getUserId())) {
            throw new BoardException(BoardErrorCode.ARTICLE_FORBIDDEN);
        }

        if (!beforeArticle.getBoard().getName().equals(request.getBoardType().name())) {
            throw new BoardException(BoardErrorCode.BOARD_CONFLICT);
        }

        beforeArticle.setTitle(request.getTitle());
        beforeArticle.setContent(request.getContent());
        beforeArticle.setUpdatedAt(OffsetDateTime.now());
        Article savedArticle = articleRepository.save(beforeArticle);

        // TODO 스토리지에 저장된 불필요한 사진 파일들에 대한 배치 스케줄러 도입 필요
        articleImgRepository.softDeleteByArticleId(articleId, OffsetDateTime.now());
        uploadAndSaveImgs(images, savedArticle);
    }

    @Transactional
    public void deleteArticle(Long articleId, String userEmail) {

        User requestUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new AuthException(UserErrorCode.USER_NOT_FOUND));

        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new BoardException(BoardErrorCode.ARTICLE_NOT_FOUND));

        if (!article.getUser().getUserId().equals(requestUser.getUserId())) {
            throw new BoardException(BoardErrorCode.ARTICLE_FORBIDDEN);
        }

        // NOTE 게시글을 삭제하면 사진, 댓글 모두 soft delete?
        OffsetDateTime deletedTime = OffsetDateTime.now();
        article.setDeletedAt(deletedTime);
        articleImgRepository.softDeleteByArticleId(articleId, deletedTime);
        replyRepository.softDeleteByArticleId(articleId, deletedTime);
    }

    private void uploadAndSaveImgs(List<MultipartFile> images, Article targetArticle) {
        if (images != null && !images.getFirst().isEmpty() ) {
            try {
                // GCP bucket 에 업로드
                List<FileDto> imgList = fileManager.upload(images, "article");

                // repository 에 저장
                List<ArticleImg> articleImgs = imgList
                    .stream()
                    .map(fileDto -> {
                        ImgType type = ImgType.DESC;
                        // NOTE 게시글을 작성할 때 썸네일을 설정할 수 있게? 아니면 첫번째 이미지?
                        if (imgList.getFirst().originName().equals(fileDto.originName())) {
                            type = ImgType.THUMBNAIL;
                        }
                        ArticleImg articleImg = ArticleImg.fromFileDto(type, fileDto);
                        articleImg.setArticle(targetArticle);
                        return articleImg;
                    })
                    .toList();

                articleImgRepository.saveAll(articleImgs);

            } catch (IOException e) {
                throw new CommonException(CommonErrorCode.FILE_UPLOAD_FAILED);
            }
        }
    }


}
