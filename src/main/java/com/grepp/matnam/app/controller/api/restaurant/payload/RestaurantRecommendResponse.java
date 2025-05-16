package com.grepp.matnam.app.controller.api.restaurant.payload;

import com.grepp.matnam.app.model.restaurant.dto.RestaurantDto;
import java.util.List;

public record RestaurantRecommendResponse(
    List<String> restaurants,
    String reason
) {

}
