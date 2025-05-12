package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {
    Page<Restaurant> findPaged(Pageable pageable);
}
