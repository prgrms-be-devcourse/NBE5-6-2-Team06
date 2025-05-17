package com.grepp.matnam.app.controller.web.admin.payload;

import java.util.Map;
import lombok.Data;

@Data
public class RestaurantStatsResponse {
    private long totalRestaurants;
    private Double averageGoogleRating;
    private long totalRecommendedCount;
    private Map<String, Long> categoryRestaurantCounts;
    private Map<String, Double> categoryAverageRatings;
    private Map<String, Long> categoryTotalRecommendedCounts;
}
