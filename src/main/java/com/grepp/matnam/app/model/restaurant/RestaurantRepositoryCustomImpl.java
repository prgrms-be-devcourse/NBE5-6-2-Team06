package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.model.restaurant.entity.QRestaurant;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
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

@RequiredArgsConstructor
public class RestaurantRepositoryCustomImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QRestaurant restaurant = QRestaurant.restaurant;

    @Override
    public Page<Restaurant> findPaged(Pageable pageable) {

        List<Restaurant> content = queryFactory
            .select(restaurant)
            .from(restaurant)
            .where(restaurant.activated)
            .orderBy(restaurant.restaurantId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(restaurant.count())
            .where(restaurant.activated)
            .from(restaurant);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Restaurant> findAll(Pageable pageable) {
        List<Restaurant> content = queryFactory
            .select(restaurant)
            .from(restaurant)
            .where(restaurant.activated)
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(restaurant.count())
            .where(restaurant.activated)
            .from(restaurant);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Restaurant> findByCategory(String category, Pageable pageable) {

        List<Restaurant> content = queryFactory
            .select(restaurant)
            .from(restaurant)
            .where(restaurant.activated)
            .where(restaurant.category.eq(category))
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(restaurant.count())
            .where(restaurant.activated)
            .where(restaurant.category.eq(category))
            .from(restaurant);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Restaurant> findByCategoryAndNameContaining(String category, String keyword,
        Pageable pageable) {
        List<Restaurant> content = queryFactory
            .select(restaurant)
            .from(restaurant)
            .where(restaurant.activated)
            .where(restaurant.category.eq(category).and(restaurant.name.contains(keyword)))
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(restaurant.count())
            .where(restaurant.activated)
            .where(restaurant.category.eq(category).and(restaurant.name.contains(keyword)))
            .from(restaurant);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Restaurant> findByNameContaining(String keyword, Pageable pageable) {
        List<Restaurant> content = queryFactory
            .select(restaurant)
            .from(restaurant)
            .where(restaurant.activated)
            .where(restaurant.name.contains(keyword))
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(restaurant.count())
            .where(restaurant.activated)
            .where(restaurant.name.contains(keyword))
            .from(restaurant);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public double averageGoogleRating() {
        return Optional.ofNullable(queryFactory
            .select(restaurant.googleRating.avg())
            .from(restaurant)
            .where(restaurant.activated.isTrue())
            .fetchOne()).orElse(0.0);
    }

    @Override
    public long sumRecommendedCount() {
        return Optional.ofNullable(queryFactory
            .select(restaurant.recommendedCount.sum())
            .from(restaurant)
            .where(restaurant.activated.isTrue())
            .fetchOne()).orElse(0);
    }

    @Override
    public double averageGoogleRatingByCategory(String category) {
        return Optional.ofNullable(queryFactory
            .select(restaurant.googleRating.avg())
            .from(restaurant)
            .where(
                restaurant.category.eq(category),
                restaurant.activated.isTrue()
            )
            .fetchOne()).orElse(0.0);
    }

    @Override
    public long sumRecommendedCountByCategory(String category) {
        return Optional.ofNullable(queryFactory
            .select(restaurant.recommendedCount.sum())
            .from(restaurant)
            .where(
                restaurant.category.eq(category),
                restaurant.activated.isTrue()
            )
            .fetchOne()).orElse(0);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        PathBuilder<Restaurant> entityPath = new PathBuilder<>(Restaurant.class, "restaurant");
        return sort.stream()
            .map(order -> {
                String property = order.getProperty();

                // 필요한 타입에 따라 캐스팅
                OrderSpecifier<?> specifier;
                if (property.equals("name")) {
                    specifier = order.isAscending()
                        ? entityPath.getString(property).asc()
                        : entityPath.getString(property).desc();
                } else if (property.equals("googleRating")) {
                    specifier = order.isAscending()
                        ? entityPath.getString(property).asc()
                        : entityPath.getString(property).desc();
                } else if (property.equals("createdAt")) {
                    specifier = order.isAscending()
                        ? entityPath.getDateTime(property, LocalDateTime.class).asc()
                        : entityPath.getDateTime(property, LocalDateTime.class).desc();
                } else {
                    // 기본: 문자열 처리
                    specifier = order.isAscending()
                        ? entityPath.getString(property).asc()
                        : entityPath.getString(property).desc();
                }

                return specifier;
            })
            .toArray(OrderSpecifier[]::new);
    }


}
