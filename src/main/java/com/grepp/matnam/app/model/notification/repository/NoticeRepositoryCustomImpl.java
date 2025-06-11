package com.grepp.matnam.app.model.notification.repository;

import com.grepp.matnam.app.model.notification.entity.Notice;
import com.grepp.matnam.app.model.notification.entity.QNotice;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QNotice notice = QNotice.notice;


    @Override
    public Page<Notice> findByKeywordContaining(String keyword, Pageable pageable) {
        List<Notice> content = queryFactory
            .select(notice)
            .from(notice)
            .where(notice.activated)
            .where(notice.message.contains(keyword))
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(notice.count())
            .where(notice.activated)
            .where(notice.message.contains(keyword))
            .from(notice);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Notice> findAllNotices(Pageable pageable) {
        List<Notice> content = queryFactory
            .select(notice)
            .from(notice)
            .where(notice.activated)
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(notice.count())
            .where(notice.activated)
            .from(notice);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
            .map(order -> {
                String property = order.getProperty();
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;

                // createdAt만 정렬에 사용한다고 가정
                if ("createdAt".equals(property)) {
                    return new OrderSpecifier<>(direction, notice.createdAt);
                }

                // 필요에 따라 다른 속성 추가
                throw new IllegalArgumentException("정렬 불가능한 속성: " + property);
            })
            .toArray(OrderSpecifier[]::new);
    }
}
