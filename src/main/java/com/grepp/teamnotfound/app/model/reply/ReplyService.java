package com.grepp.teamnotfound.app.model.reply;

import com.grepp.teamnotfound.app.controller.api.article.payload.PageInfo;
import com.grepp.teamnotfound.app.controller.api.reply.payload.ReplyDetailResponse;
import com.grepp.teamnotfound.app.controller.api.reply.payload.ReplyListResponse;
import com.grepp.teamnotfound.app.controller.api.reply.payload.ReplyRequest;
import com.grepp.teamnotfound.app.model.board.entity.Article;
import com.grepp.teamnotfound.app.model.board.repository.ArticleRepository;
import com.grepp.teamnotfound.app.model.reply.entity.Reply;
import com.grepp.teamnotfound.app.model.reply.repository.ReplyRepository;
import com.grepp.teamnotfound.app.model.user.UserService;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.entity.UserImg;
import com.grepp.teamnotfound.app.model.user.repository.UserImgRepository;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.error.exception.AuthException;
import com.grepp.teamnotfound.infra.error.exception.BoardException;
import com.grepp.teamnotfound.infra.error.exception.code.BoardErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final UserImgRepository userImgRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public ReplyDetailResponse createReply(ReplyRequest request, Long articleId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AuthException(UserErrorCode.USER_NOT_FOUND));
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new BoardException(BoardErrorCode.ARTICLE_NOT_FOUND));

        String profileImgPath = userService.getProfileImgPath(userId);

        Reply reply = Reply.builder()
            .article(article)
            .user(user)
            .content(request.getContent())
            .build();
        Reply savedReply = replyRepository.save(reply);

        return ReplyDetailResponse.builder()
            .articleId(articleId)
            .replyId(savedReply.getReplyId())
            .nickname(user.getNickname())
            .profileImgPath(profileImgPath)
            .content(savedReply.getContent())
            .createdAt(savedReply.getCreatedAt())
            .build();
    }

    @Transactional
    public ReplyDetailResponse updateReply(ReplyRequest request, Long articleId, Long replyId,
        Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AuthException(UserErrorCode.USER_NOT_FOUND));
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new BoardException(BoardErrorCode.ARTICLE_NOT_FOUND));
        Reply reply = replyRepository.findById(replyId)
            .orElseThrow(() -> new BoardException(BoardErrorCode.REPLY_NOT_FOUND));

        if (!reply.getUser().getUserId().equals(userId)) {
            throw new BoardException(BoardErrorCode.REPLY_FORBIDDEN);
        }

        reply.setContent(request.getContent());
        reply.setUpdatedAt(OffsetDateTime.now());
        Reply savedReply = replyRepository.save(reply);

        String profileImgPath = userService.getProfileImgPath(userId);

        return ReplyDetailResponse
            .builder()
            .articleId(article.getArticleId())
            .replyId(savedReply.getReplyId())
            .nickname(user.getNickname())
            .profileImgPath(profileImgPath)
            .content(savedReply.getContent())
            .createdAt(savedReply.getCreatedAt())
            .updatedAt(savedReply.getUpdatedAt())
            .build();
    }

    @Transactional
    public void deleteReply(Long articleId, Long replyId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AuthException(UserErrorCode.USER_NOT_FOUND));
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new BoardException(BoardErrorCode.ARTICLE_NOT_FOUND));
        Reply reply = replyRepository.findById(replyId)
            .orElseThrow(() -> new BoardException(BoardErrorCode.REPLY_NOT_FOUND));

        if (!reply.getUser().getUserId().equals(userId)) {
            throw new BoardException(BoardErrorCode.REPLY_FORBIDDEN);
        }

        reply.setDeletedAt(OffsetDateTime.now());
        replyRepository.save(reply);
    }

    @Transactional(readOnly = true)
    public ReplyListResponse getReplies(Long articleId, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Direction.DESC, "createdAt"));
        Page<Reply> replyPage = replyRepository.findByArticle_ArticleIdAndDeletedAtIsNullAndReportedAtIsNull(articleId, pageable);

        List<ReplyDetailResponse> replyList = replyPage.stream()
            .map(reply -> {
                String profileImgPath = userService.getProfileImgPath(reply.getUser().getUserId());
                return ReplyDetailResponse.builder()
                    .articleId(articleId)
                    .replyId(reply.getReplyId())
                    .nickname(reply.getUser().getNickname())
                    .profileImgPath(profileImgPath)
                    .content(reply.getContent())
                    .createdAt(reply.getCreatedAt())
                    .updatedAt(reply.getUpdatedAt())
                    .build();
            })
            .toList();

        return ReplyListResponse.builder()
            .replyList(replyList)
            .pageInfo(PageInfo.fromPage(replyPage))
            .build();
    }
}
