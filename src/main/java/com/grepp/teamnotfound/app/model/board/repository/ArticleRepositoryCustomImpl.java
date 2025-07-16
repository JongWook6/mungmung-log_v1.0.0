package com.grepp.teamnotfound.app.model.board.repository;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.app.model.board.dto.ArticleImgDto;
import com.grepp.teamnotfound.app.model.board.dto.ArticleListDto;
import com.grepp.teamnotfound.app.model.board.entity.QArticle;
import com.grepp.teamnotfound.app.model.board.entity.QArticleImg;
import com.grepp.teamnotfound.app.model.board.entity.QArticleLike;
import com.grepp.teamnotfound.app.model.reply.entity.QReply;
import com.grepp.teamnotfound.app.model.user.entity.QUser;
import com.grepp.teamnotfound.app.model.user.entity.QUserImg;
import com.grepp.teamnotfound.infra.code.ImgType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QArticle article = QArticle.article;
    QUser user = QUser.user;
    QUserImg userImg = QUserImg.userImg;
    QArticleImg articleImg = QArticleImg.articleImg;
    QArticleLike articleLike = QArticleLike.articleLike;
    QReply reply = QReply.reply;

    @Override
    public ArticleDetailResponse findDetailById(Long articleId, Long loginUserId) {
        // 서브 쿼리: 댓글 수 (삭제되거나 신고된 댓글 제외)
        JPQLQuery<Integer> replyCount = JPAExpressions
            .select(reply.count().intValue())
            .from(reply)
            .where(
                reply.article.articleId.eq(articleId),
                reply.deletedAt.isNull(),
                reply.reportedAt.isNull()
            );

        // 서브 쿼리: 좋아요 수
        JPQLQuery<Integer> likeCount = JPAExpressions
            .select(articleLike.count().intValue())
            .from(articleLike)
            .where(articleLike.article.articleId.eq(articleId));

        // 서브 쿼리: 로그인 사용자가 좋아요를 눌렀는지 여부
        BooleanExpression isLiked;
        if (loginUserId != null) {
            isLiked = JPAExpressions
                .selectOne()
                .from(articleLike)
                .where(
                    articleLike.article.articleId.eq(articleId),
                    articleLike.user.userId.eq(loginUserId)
                )
                .exists();
        } else {
            isLiked = Expressions.FALSE;
        }

        // 메인 쿼리
        Tuple result = queryFactory
            .select(
                article.articleId,
                user.nickname,
                userImg.savePath.append(userImg.renamedName),
                article.createdAt,
                article.updatedAt,
                article.title,
                article.content,
                replyCount,
                likeCount,
                article.views,
                isLiked
            )
            .from(article)
            .join(article.user, user)
            .leftJoin(userImg).on( // userImg 가 없을 수도 있으므로 leftJoin
                userImg.user.userId.eq(user.userId),
                userImg.deletedAt.isNull()
            )
            .where(
                article.articleId.eq(articleId)
            )
            .fetchOne();

        if (result == null) {
            return null;
        }

        // 이미지 리스트는 별도로 조회
        List<ArticleImgDto> images = queryFactory
            .select(
                Projections.constructor(
                    ArticleImgDto.class,
                    articleImg.articleImgId,
                    articleImg.savePath.append(articleImg.renamedName),
                    articleImg.type
                )
            )
            .from(articleImg)
            .where(
                articleImg.article.articleId.eq(articleId),
                articleImg.deletedAt.isNull()
            )
            .fetch();

        return new ArticleDetailResponse(
            result.get(article.articleId),
            result.get(user.nickname),
            result.get(userImg.savePath.append(userImg.renamedName)),
            result.get(article.createdAt),
            result.get(article.updatedAt),
            result.get(article.title),
            result.get(article.content),
            result.get(replyCount),
            result.get(likeCount),
            result.get(article.views),
            result.get(article.deletedAt.isNull()),
            result.get(article.reportedAt.isNull()),
            result.get(isLiked),
            images
        );
    }

    @Override
    public Page<ArticleListDto> findArticleListWithMeta(int page, int size) {

        // 서브쿼리 : 좋아요 수
        JPQLQuery<Integer> likes = JPAExpressions.select(articleLike.count().intValue())
            .from(articleLike)
            .where(articleLike.article.articleId.eq(article.articleId));

        // 서브쿼리 : 댓글 수
        JPQLQuery<Integer> replies = JPAExpressions.select(reply.count().intValue())
            .from(reply)
            .where(
                reply.article.articleId.eq(article.articleId),
                reply.deletedAt.isNull(),
                reply.reportedAt.isNull()
            );

        // 서브쿼리 : 썸네일 이미지 경로
        JPQLQuery<String> thumbnailImgPath = JPAExpressions.select(
                articleImg.savePath.append(articleImg.renamedName).stringValue()
            )
            .from(articleImg)
            .where(
                articleImg.article.articleId.eq(article.articleId),
                articleImg.deletedAt.isNull(),
                articleImg.type.eq(ImgType.THUMBNAIL)
            )
            .orderBy(articleImg.createdAt.desc()) // 가장 최근 썸네일 하나
            .limit(1);


        // 메인 쿼리
        List<ArticleListDto> content = queryFactory
            .select(Projections.fields(
                ArticleListDto.class,
                article.articleId,
                user.nickname,
                userImg.savePath.append(userImg.renamedName).as("profileImgPath"),
                article.createdAt,
                article.updatedAt,
                article.title,
                article.content,
                ExpressionUtils.as(likes, "likes"),
                ExpressionUtils.as(replies, "replies"),
                article.views,
                ExpressionUtils.as(thumbnailImgPath, "thumbnailImgPath")
            ))
            .from(article)
            .join(article.user, user)
            .leftJoin(userImg).on(
                userImg.user.userId.eq(user.userId),
                userImg.deletedAt.isNull()
            )
            .where(
                article.deletedAt.isNull(),
                article.reportedAt.isNull()
            )
            .orderBy(article.createdAt.desc()) // TODO 동적으로 정렬 필요
            .offset((long) page * size)
            .limit(size)
            .fetch();

        // 페이징을 위한 총 개수 조회
        Long total = queryFactory
            .select(article.count())
            .from(article)
            .where(article.deletedAt.isNull(), article.reportedAt.isNull())
            .fetchOne();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }
}
