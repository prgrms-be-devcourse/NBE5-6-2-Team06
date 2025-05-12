package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.model.restaurant.code.Category;
import com.grepp.matnam.app.model.restaurant.entity.QRestaurant;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class RestaurantRepositoryCustomImpl implements RestaurantRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QRestaurant restaurant = QRestaurant.restaurant;

    @Override
    public Page<Restaurant> findPaged(Pageable pageable) {

        List<Restaurant> content = queryFactory
            .select(restaurant)
            .from(restaurant)
            .orderBy(restaurant.restaurantId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(restaurant.count())
            .from(restaurant);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Restaurant> findByCategory(Category category, Pageable pageable) {
        List<Restaurant> content = queryFactory
            .select(restaurant)
            .from(restaurant)
            .where(restaurant.category.eq(category.getKoreanName()))
            .orderBy(restaurant.restaurantId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(restaurant.count())
            .where(restaurant.category.eq(category.getKoreanName()))
            .from(restaurant);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

}
