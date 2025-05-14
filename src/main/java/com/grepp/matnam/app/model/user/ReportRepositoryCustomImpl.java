package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.dto.ReportDto;
import com.grepp.matnam.app.model.user.entity.QReport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QReport report = QReport.report;

    @Override
    public Page<ReportDto> findAllReports(Pageable pageable) {
        List<ReportDto> content = queryFactory
            .select(Projections.constructor(ReportDto.class,
                report.reportId,
                report.user.userId,
                report.reportedId,
                report.reason,
                report.createdAt,
                report.activated
            ))
            .from(report)
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(report.count())
            .from(report);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ReportDto> findByStatusAndKeywordContaining(Boolean status, String keyword,
        Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(keyword)) {
            builder.and(
                report.user.userId.containsIgnoreCase(keyword)
                    .or(report.reportedId.containsIgnoreCase(keyword))
            );
        }

        List<ReportDto> content = queryFactory
            .select(Projections.constructor(ReportDto.class,
                report.reportId,
                report.user.userId,
                report.reportedId,
                report.reason,
                report.createdAt,
                report.activated
            ))
            .from(report)
            .where(report.activated.eq(status))
            .where(builder)
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(report.count())
            .where(report.activated.eq(status))
            .where(builder)
            .from(report);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ReportDto> findByStatus(Boolean status, Pageable pageable) {

        List<ReportDto> content = queryFactory
            .select(Projections.constructor(ReportDto.class,
                report.reportId,
                report.user.userId,
                report.reportedId,
                report.reason,
                report.createdAt,
                report.activated
            ))
            .from(report)
            .where(report.activated.eq(status))
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(report.count())
            .where(report.activated.eq(status))
            .from(report);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ReportDto> findByKeywordContaining(String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(keyword)) {
            builder.and(
                report.user.userId.containsIgnoreCase(keyword)
                    .or(report.reportedId.containsIgnoreCase(keyword))
            );
        }

        List<ReportDto> content = queryFactory
            .select(Projections.constructor(ReportDto.class,
                report.reportId,
                report.user.userId,
                report.reportedId,
                report.reason,
                report.createdAt,
                report.activated
            ))
            .from(report)
            .where(builder)
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(report.count())
            .where(builder)
            .from(report);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
            .map(order -> {
                String property = order.getProperty();
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;

                // createdAt만 정렬에 사용한다고 가정
                if ("createdAt".equals(property)) {
                    return new OrderSpecifier<>(direction, report.createdAt);
                }

                // 필요에 따라 다른 속성 추가
                throw new IllegalArgumentException("정렬 불가능한 속성: " + property);
            })
            .toArray(OrderSpecifier[]::new);
    }
}
