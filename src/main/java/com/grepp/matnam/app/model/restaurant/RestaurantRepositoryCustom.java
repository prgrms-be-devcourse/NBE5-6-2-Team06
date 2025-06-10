package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {
    Page<Restaurant> findPaged(Pageable pageable);

    Page<Restaurant> findAll(Pageable pageable);

    Page<Restaurant> findByCategory(String category, Pageable pageable);

    Page<Restaurant> findByCategoryAndNameContaining(String category, String keyword, Pageable pageable);

    Page<Restaurant> findByNameContaining(String keyword, Pageable pageable);

    double averageGoogleRating();

    long sumRecommendedCount();

    double averageGoogleRatingByCategory(String category);

    long sumRecommendedCountByCategory(String category);
}
