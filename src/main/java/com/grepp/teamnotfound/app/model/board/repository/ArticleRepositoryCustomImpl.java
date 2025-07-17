package com.grepp.teamnotfound.app.model.board.repository;

import com.grepp.teamnotfound.app.controller.api.article.payload.ArticleDetailResponse;
import com.grepp.teamnotfound.app.model.board.code.SortType;
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
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

        // 서브 쿼리 (로그인 사용자가 좋아요를 눌렀는지 여부)
        // 단일 게시글에 대한 확인이므로 GROUP BY 후 집계하는 것보다 별도로 빠르게 확인
        BooleanExpression isLikedSubquery;
        if (loginUserId != null) {
            isLikedSubquery = JPAExpressions
                .selectOne()
                .from(articleLike)
                .where(
                    articleLike.article.articleId.eq(articleId),
                    articleLike.user.userId.eq(loginUserId)
                )
                .exists();
        } else {
            isLikedSubquery = Expressions.FALSE;
        }

        // 메인 쿼리
        ArticleDetailResponse detailResponse = queryFactory.select(Projections.fields(
                ArticleDetailResponse.class,
                article.articleId,
                user.nickname,
                userImg.savePath.append(userImg.renamedName).as("profileImgPath"),
                article.createdAt,
                article.updatedAt,
                article.title,
                article.content,
                // LEFT JOIN 후 COUNT DISTINCT 로 집계
                articleLike.countDistinct().intValue().as("likes"),
                reply.countDistinct().intValue().as("replies"),
                article.views,
                isLikedSubquery.as("isLiked")
            ))
            .from(article)
            .join(article.user, user)
            // LEFT JOIN 으로 좋아요, 댓글, 이미지가 없는 게시글도 포함
            .leftJoin(articleLike).on(
                articleLike.article.articleId.eq(article.articleId)
            )
            .leftJoin(reply).on(
                reply.article.articleId.eq(article.articleId),
                reply.deletedAt.isNull(),
                reply.reportedAt.isNull()
            )
            .leftJoin(userImg).on(
                userImg.user.userId.eq(user.userId),
                userImg.deletedAt.isNull()
            )
            .where(
                article.articleId.eq(articleId),
                article.deletedAt.isNull(),
                article.reportedAt.isNull()
            )
            // 집계 함수가 아닌 필드는 모두 GROUP BY 절에 포함
            // isLiked 는 서브쿼리이므로 미포함
            .groupBy(
                article.articleId,
                user.nickname,
                userImg.savePath,
                userImg.renamedName,
                article.createdAt,
                article.updatedAt,
                article.title,
                article.content,
                article.views
            )
            .fetchOne();

        if (detailResponse == null) {
            return null;
        }

        // 이미지 리스트는 별도로 조회
        List<ArticleImgDto> images = queryFactory
            .select(
                Projections.constructor(
                    ArticleImgDto.class,
                    articleImg.article.articleId,
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

        detailResponse.setImages(images);
        return detailResponse;
    }

    @Override
    public Page<ArticleListDto> findArticleListWithMeta(int page, int size, SortType sortType) {

        // 정렬 기준 리스트
        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(sortType);

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
                // COUNT DISTINCT 필수 (한 게시글에 좋아요 2개, 댓글 3개 -> 총 6개 로 집계됨)
                articleLike.countDistinct().intValue().as("likes"), // 기본적으로 long 타입 반환
                reply.countDistinct().intValue().as("replies"),
                article.views.intValue()
            ))
            .from(article)
            .join(article.user, user)
            // LEFT JOIN 으로 좋아요, 댓글, 사진이 없는 게시글도 포함
            .leftJoin(articleLike).on(
                articleLike.article.articleId.eq(article.articleId)
            )
            .leftJoin(reply).on(
                reply.article.articleId.eq(article.articleId),
                reply.deletedAt.isNull(),
                reply.reportedAt.isNull()
            )
            .leftJoin(userImg).on(
                userImg.user.userId.eq(user.userId),
                userImg.deletedAt.isNull()
            )
            .where(
                article.deletedAt.isNull(),
                article.reportedAt.isNull()
            )
            // 집계 함수가 아닌 필드는 모두 GROUP BY 절에 포함
            .groupBy(
                article.articleId,
                user.nickname,
                userImg.savePath,
                userImg.renamedName,
                article.createdAt,
                article.updatedAt,
                article.title,
                article.content,
                article.views
            )
            .orderBy(orderSpecifiers) // 동적으로 정렬 적용
            .offset((long) page * size)
            .limit(size)
            .fetch();

        // 게시글 id 목록
        List<Long> articleIds = content.stream().map(ArticleListDto::getArticleId).toList();

        if (!articleIds.isEmpty()) {
            // 게시글 이미지 조회
            List<ArticleImgDto> thumbnails = queryFactory.select(Projections.constructor(
                    ArticleImgDto.class,
                    articleImg.articleImgId,
                    article.articleId,
                    articleImg.savePath.append(articleImg.renamedName),
                    articleImg.type
                ))
                .from(articleImg)
                .where(
                    articleImg.article.articleId.in(articleIds),
                    articleImg.deletedAt.isNull(),
                    articleImg.type.eq(ImgType.THUMBNAIL)
                )
                .orderBy(articleImg.createdAt.desc()) // 가장 최근 썸네일
                .fetch();

            Map<Long, ArticleImgDto> thumbnailMap = new LinkedHashMap<>();
            for (ArticleImgDto imgDto : thumbnails) {
                thumbnailMap.putIfAbsent(imgDto.getArticleId(), imgDto);
            }

            for (ArticleListDto articleDto : content) {
                ArticleImgDto thumbnail = thumbnailMap.get(articleDto.getArticleId());
                if (thumbnail != null) {
                    articleDto.setArticleImgPath(List.of(thumbnail));
                } else {
                    articleDto.setArticleImgPath(List.of());
                }
            }
        }

        // 페이징을 위한 총 개수 조회
        Long total = queryFactory
            .select(article.count())
            .from(article)
            .where(article.deletedAt.isNull(), article.reportedAt.isNull())
            .fetchOne();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

    // SortType -> Querydsl 정렬 기준으로 매칭
    private OrderSpecifier<?>[] getOrderSpecifiers(SortType sortType) {
        return switch (sortType) {
            case DATE -> new OrderSpecifier[]{article.createdAt.desc()};
            case LIKE -> new OrderSpecifier[]{articleLike.countDistinct().desc()};
            case VIEW -> new OrderSpecifier[]{article.views.desc()};
        };
    }
}
