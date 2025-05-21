package com.grepp.matnam.app.controller.api.restaurant.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRecommendRequest {

    private List<String> teamKeywords; // 집계된 키워드

}
