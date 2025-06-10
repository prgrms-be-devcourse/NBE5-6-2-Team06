package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.dto.MonthlyMeetingStatsDto;
import com.grepp.matnam.app.model.team.dto.ParticipantWithUserIdDto;
import com.grepp.matnam.app.model.team.entity.QParticipant;
import com.grepp.matnam.app.model.team.entity.QTeam;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QTeam team = QTeam.team;
    private final QParticipant participant = QParticipant.participant;
    private final QUser user = QUser.user;

    @Override
    public Page<Team> findAllUsers(Pageable pageable) {
        List<Team> content = queryFactory
            .select(team)
            .from(team)
            .where(team.activated)
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(team.count())
            .where(team.activated)
            .from(team);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Team> findByStatusAndKeywordContaining(String status, String keyword,
        Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(keyword)) {
            builder.and(
                team.user.userId.containsIgnoreCase(keyword)
                    .or(team.teamTitle.containsIgnoreCase(keyword))
            );
        }

        List<Team> content = queryFactory
            .select(team)
            .from(team)
            .where(team.activated)
            .where(builder.and(team.status.eq(Status.valueOf(status))))
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(team.count())
            .where(team.activated)
            .where(builder.and(team.status.eq(Status.valueOf(status))))
            .from(team);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Team> findByStatus(String status, Pageable pageable) {
        List<Team> content = queryFactory
            .select(team)
            .from(team)
            .where(team.activated)
            .where(team.status.eq(Status.valueOf(status)))
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(team.count())
            .where(team.activated)
            .where(team.status.eq(Status.valueOf(status)))
            .from(team);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Team> findByKeywordContaining(String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(keyword)) {
            builder.and(
                team.user.userId.containsIgnoreCase(keyword)
                    .or(team.teamTitle.containsIgnoreCase(keyword))
            );
        }

        List<Team> content = queryFactory
            .select(team)
            .from(team)
            .where(team.activated)
            .where(builder)
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(team.count())
            .where(team.activated)
            .where(builder)
            .from(team);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<MonthlyMeetingStatsDto> findMonthlyMeetingStats(LocalDateTime startDate) {
        Expression<String> monthExpr = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", team.teamDate);
        NumberExpression<Long> completedMeetings = new CaseBuilder()
            .when(team.status.eq(Status.COMPLETED))
            .then(1L)
            .otherwise(0L)
            .sum();

        return queryFactory
            .select(Projections.constructor(
                MonthlyMeetingStatsDto.class,
                monthExpr,
                team.count(),
                completedMeetings
            ))
            .from(team)
            .where(
                team.teamDate.goe(startDate),
                team.activated.isTrue()
            )
            .groupBy(monthExpr)
            .orderBy(new OrderSpecifier<>(Order.ASC, monthExpr))
            .fetch();
    }

    @Override
    public double averageMaxPeopleForActiveTeams() {
        return Optional.ofNullable(
            queryFactory
                .select(team.maxPeople.avg())
                .from(team)
                .where(
                    team.status.in(Status.RECRUITING, Status.FULL),
                    team.activated.isTrue()
                )
                .fetchOne()
        ).orElse(0.0);
    }

    @Override
    public List<ParticipantWithUserIdDto> findAllDtoByTeamId(Long teamId) {
        return queryFactory
            .select(Projections.constructor(
                ParticipantWithUserIdDto.class,
                participant.participantId,
                user.userId
            ))
            .from(participant)
            .join(participant.user, user)
            .where(
                participant.team.teamId.eq(teamId),
                participant.participantStatus.eq(ParticipantStatus.APPROVED)
            )
            .fetch();
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
            .map(order -> {
                String property = order.getProperty();
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;

                // createdAt만 정렬에 사용한다고 가정
                if ("createdAt".equals(property)) {
                    return new OrderSpecifier<>(direction, team.createdAt);
                }

                // 필요에 따라 다른 속성 추가
                throw new IllegalArgumentException("정렬 불가능한 속성: " + property);
            })
            .toArray(OrderSpecifier[]::new);
    }
}
