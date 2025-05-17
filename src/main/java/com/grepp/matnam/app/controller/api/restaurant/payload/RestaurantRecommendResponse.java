package com.grepp.matnam.app.controller.api.restaurant.payload;

import java.util.List;

public record RestaurantRecommendResponse(
    List<String> restaurants,
    String reason
) {

}
