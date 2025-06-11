package com.grepp.matnam.app.model.content.repository;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.grepp.matnam.app.model.content.entity.QContentRanking.contentRanking;

@Repository
@RequiredArgsConstructor
public class ContentRankingRepositoryCustomImpl implements ContentRankingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ContentRanking> findActiveRankingsForToday() {
        LocalDate today = LocalDate.now();

        return queryFactory
                .selectFrom(contentRanking)
                .where(getActiveRankingCondition(today))
                .orderBy(contentRanking.createdAt.desc())
                .fetch();
    }

    @Override
    public Page<ContentRanking> findActiveRankingsForToday(Pageable pageable) {
        LocalDate today = LocalDate.now();

        List<ContentRanking> content = queryFactory
                .selectFrom(contentRanking)
                .where(getActiveRankingCondition(today))
                .orderBy(contentRanking.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(contentRanking.count())
                .from(contentRanking)
                .where(getActiveRankingCondition(today))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    @Override
    public List<ContentRanking> findActiveRankingsForDate(LocalDate date) {
        return queryFactory
                .selectFrom(contentRanking)
                .where(getActiveRankingCondition(date))
                .orderBy(contentRanking.createdAt.desc())
                .fetch();
    }

    @Override
    public Optional<ContentRanking> findFirstActiveRankingForToday() {
        LocalDate today = LocalDate.now();

        ContentRanking result = queryFactory
                .selectFrom(contentRanking)
                .where(getActiveRankingCondition(today))
                .orderBy(contentRanking.createdAt.desc())
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<ContentRanking> searchRankings(String title, Boolean isActive,
                                               String sortBy, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(title)) {
            builder.and(contentRanking.title.containsIgnoreCase(title));
        }

        if (isActive != null) {
            builder.and(contentRanking.isActive.eq(isActive));
        }

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy);

        List<ContentRanking> content = queryFactory
                .selectFrom(contentRanking)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(contentRanking.count())
                .from(contentRanking)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    @Override
    public long deactivateExpiredRankings() {
        LocalDate today = LocalDate.now();

        return queryFactory
                .update(contentRanking)
                .set(contentRanking.isActive, false)
                .where(
                        contentRanking.isActive.isTrue()
                                .and(contentRanking.endDate.isNotNull())
                                .and(contentRanking.endDate.lt(today))
                )
                .execute();
    }

    @Override
    public List<ContentRanking> findRecentRankings(int limit) {
        return queryFactory
                .selectFrom(contentRanking)
                .orderBy(contentRanking.createdAt.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<ContentRanking> findAllByOrderByCreatedAtDesc() {
        return queryFactory
                .selectFrom(contentRanking)
                .orderBy(contentRanking.createdAt.desc())
                .fetch();
    }

    private BooleanBuilder getActiveRankingCondition(LocalDate date) {
        return new BooleanBuilder()
                .and(contentRanking.isActive.isTrue())
                .and(contentRanking.startDate.loe(date))
                .and(contentRanking.endDate.isNull()
                        .or(contentRanking.endDate.goe(date)));
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy) {
        if (sortBy == null) {
            return contentRanking.createdAt.desc();
        }

        return switch (sortBy.toLowerCase()) {
            case "title" -> contentRanking.title.asc();
            case "title_desc" -> contentRanking.title.desc();
            case "start_date" -> contentRanking.startDate.asc();
            case "start_date_desc" -> contentRanking.startDate.desc();
            case "oldest" -> contentRanking.createdAt.asc();
            default -> contentRanking.createdAt.desc(); // newest (기본값)
        };
    }
}