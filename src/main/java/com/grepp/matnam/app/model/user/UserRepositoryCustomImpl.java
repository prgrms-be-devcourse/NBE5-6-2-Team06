package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.code.Gender;
import com.grepp.matnam.app.model.user.code.Status;
import com.grepp.matnam.app.model.user.entity.QUser;
import com.grepp.matnam.app.model.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;
    private final ModelMapper mapper;


    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        List<User> content = queryFactory
            .select(user)
            .from(user)
            .where(user.activated)
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .where(user.activated)
            .from(user);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<User> findByStatusAndKeywordContaining(String status, String keyword,
        Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(keyword)) {
            builder.and(
                user.nickname.containsIgnoreCase(keyword)
                    .or(user.email.containsIgnoreCase(keyword))
                    .or(user.userId.containsIgnoreCase(keyword))
            );
        }

        List<User> content = queryFactory
            .select(user)
            .from(user)
            .where(user.activated)
            .where(builder.and(user.status.eq(Status.valueOf(status))))
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .where(user.activated)
            .where(builder.and(user.status.eq(Status.valueOf(status))))
            .from(user);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<User> findByStatus(String status, Pageable pageable) {

        List<User> content = queryFactory
            .select(user)
            .from(user)
            .where(user.activated)
            .where(user.status.eq(Status.valueOf(status)))
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .where(user.activated)
            .where(user.status.eq(Status.valueOf(status)))
            .from(user);


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<User> findByKeywordContaining(String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(keyword)) {
            builder.and(
                user.nickname.containsIgnoreCase(keyword)
                    .or(user.email.containsIgnoreCase(keyword))
                    .or(user.userId.containsIgnoreCase(keyword))
            );
        }

        List<User> content = queryFactory
            .select(user)
            .from(user)
            .where(user.activated)
            .where(builder)
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .where(user.activated)
            .where(builder)
            .from(user);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Integer> findAllAges() {
        return queryFactory
            .select(user.age)
            .from(user)
            .where(user.activated.isTrue())
            .fetch();
    }

    @Override
    public List<Gender> findAllGenders() {
        return queryFactory
            .select(user.gender)
            .from(user)
            .fetch();
    }

    @Override
    public long countByStatusNotActive() {
        return Optional.ofNullable(queryFactory
            .select(user.count())
            .from(user)
            .where(user.status.ne(Status.ACTIVE))
            .fetchOne()).orElse(0L);
    }

    @Override
    public long countByGenderAndStatusNotActive(Gender gender) {
        return Optional.ofNullable(queryFactory
            .select(user.count())
            .from(user)
            .where(
                user.gender.eq(gender),
                user.status.ne(Status.ACTIVE)
            )
            .fetchOne()).orElse(0L);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
            .map(order -> {
                String property = order.getProperty();
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;

                // createdAt만 정렬에 사용한다고 가정
                if ("createdAt".equals(property)) {
                    return new OrderSpecifier<>(direction, user.createdAt);
                }

                // 필요에 따라 다른 속성 추가
                throw new IllegalArgumentException("정렬 불가능한 속성: " + property);
            })
            .toArray(OrderSpecifier[]::new);
    }
}
