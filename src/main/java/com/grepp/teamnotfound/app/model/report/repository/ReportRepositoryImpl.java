package com.grepp.teamnotfound.app.model.report.repository;

import com.grepp.teamnotfound.app.controller.api.admin.code.AdminListSortDirection;
import com.grepp.teamnotfound.app.controller.api.admin.code.ReportStateFilter;
import com.grepp.teamnotfound.app.controller.api.admin.code.ReportsListSortBy;
import com.grepp.teamnotfound.app.controller.api.admin.payload.ReportsListRequest;
import com.grepp.teamnotfound.app.model.report.code.ReportState;
import com.grepp.teamnotfound.app.model.report.code.ReportType;
import com.grepp.teamnotfound.app.model.report.dto.ReportsListDto;
import com.grepp.teamnotfound.app.model.report.entity.QReport;
import com.grepp.teamnotfound.app.model.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QReport report = QReport.report;
    QUser user = QUser.user;

    @Override
    public Page<ReportsListDto> findReportListWithMeta(ReportsListRequest request, Pageable pageable) {

        QUser reporterUser = new QUser("reporterUser");
        QUser reportedUser = new QUser("reportedUser");

        // 검색 조건 : search - 신고자, 신고대상자의 닉네임
        BooleanBuilder search = new BooleanBuilder();
        if(request.getSearch() != null && !request.getSearch().isEmpty()) {
            search.and(
                    reporterUser.nickname.containsIgnoreCase(request.getSearch())
                            .or(reportedUser.nickname.containsIgnoreCase(request.getSearch()))
            );
        }

        // 필터링 조건 : search.and - ALL, PENDING, REJECT, ACCEPT
        if (request.getStatus() != null && request.getStatus() != ReportStateFilter.ALL) {
            switch (request.getStatus()) {
                case PENDING -> search.and(report.state.eq(ReportState.PENDING));
                case ACCEPT -> search.and(report.state.eq(ReportState.ACCEPT));
                case REJECT -> search.and(report.state.eq(ReportState.REJECT));
            }
        }

        // 메인 쿼리
        List<ReportsListDto> content = queryFactory
                .select(Projections.fields(
                        ReportsListDto.class,
                        report.reportId,
                        reporterUser.nickname.as("reporter"),// 신고자 닉네임
                        report.type,
                        reportedUser.nickname.as("reported"),// 신고 대상자 닉네임
                        report.createdAt,
                        report.category,
                        report.reason,
                        report.state.as("status")
                ))
                .from(report)
                .where(search)
                .leftJoin(report.reporter, reporterUser)
                .leftJoin(report.reported, reportedUser)
                .orderBy(getOrderSpecifier(request.getSortBy(), request.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count
        Long total = queryFactory
                .select(report.count())
                .from(report)
                .leftJoin(report.reporter, reporterUser)
                .leftJoin(report.reported, reportedUser)
                .where(search)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }

    private OrderSpecifier<?> getOrderSpecifier(ReportsListSortBy sortBy, AdminListSortDirection direction) {

        if(sortBy==null){
            return report.createdAt.asc();
        }

        return switch (sortBy){
            case REPORTED_AT -> direction.isAsc() ? report.createdAt.asc() : report.createdAt.desc();
            case REPORTER_NICKNAME -> direction.isAsc() ? report.reporter.nickname.asc() : report.reporter.nickname.desc();
            case REPORTED_NICKNAME -> direction.isAsc() ? report.reported.nickname.asc() : report.reported.nickname.desc();
            case CONTENT_TYPE -> {
                NumberExpression<Integer> typeOrder = new CaseBuilder()
                        .when(report.type.eq(ReportType.REPLY)).then(2)
                        .otherwise(1);
                yield direction.isAsc() ? typeOrder.asc() : typeOrder.desc();
            }
            case REASON -> direction.isAsc()? report.reason.asc() : report.reason.desc();
            case STATUS -> {
                NumberExpression<Integer> statusOrder = new CaseBuilder()
                        .when(report.state.eq(ReportState.REJECT)).then(3)
                        .when(report.state.eq(ReportState.ACCEPT)).then(2)
                        .otherwise(1);
                yield direction.isAsc() ? statusOrder.asc() : statusOrder.desc();
            }
        };
    }
}
