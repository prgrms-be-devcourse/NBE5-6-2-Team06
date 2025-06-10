package com.grepp.matnam.app.model.log;

import com.grepp.matnam.app.controller.api.admin.payload.StatLongResponse;
import com.grepp.matnam.app.model.log.entity.QUserActivityLog;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserActivityLogRepositoryCustomImpl implements UserActivityLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUserActivityLog log = QUserActivityLog.userActivityLog;

    @Override
    public List<StatLongResponse> findMonthlyUserActivity(LocalDate startDate) {
        Expression<String> labelExpr = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')",
            log.activityDate);

        return queryFactory
            .select(Projections.constructor(
                StatLongResponse.class,
                labelExpr,
                log.userId.countDistinct()
            ))
            .from(log)
            .where(log.activityDate.goe(startDate))
            .groupBy(labelExpr)
            .orderBy(new OrderSpecifier<>(Order.ASC, labelExpr))
            .fetch();
    }

    @Override
    public List<StatLongResponse> findWeekUserActivity(LocalDate startDate) {
        Expression<String> labelExpr = Expressions.stringTemplate("DATE_FORMAT({0}, '%m-%d')",
            log.activityDate);

        return queryFactory
            .select(Projections.constructor(
                StatLongResponse.class,
                labelExpr,
                log.userId.countDistinct()
            ))
            .from(log)
            .where(log.activityDate.goe(startDate))
            .groupBy(labelExpr)
            .orderBy(new OrderSpecifier<>(Order.ASC, labelExpr))
            .fetch();
    }
}
