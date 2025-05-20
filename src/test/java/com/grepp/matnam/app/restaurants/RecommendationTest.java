package com.grepp.matnam.app.restaurants;

import com.grepp.matnam.app.controller.api.restaurant.RestaurantApiController;
import com.grepp.matnam.app.controller.api.restaurant.payload.RestaurantRecommendResponse;
import com.grepp.matnam.infra.response.ApiResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class RecommendationTest {

    @Autowired
    private RestaurantApiController restaurantApiController;

    @Test
    public void testRecommend() {
        Long teamId = 1L;

        ApiResponse<RestaurantRecommendResponse> response = restaurantApiController.recommend(teamId);

        RestaurantRecommendResponse data = response.data();

        if (data != null) {
            List<String> restaurants = data.restaurants();
            String reason = data.reason();

            log.info("식당 리스트: {}", restaurants);
            log.info("이유: {}", reason);

        } else {
            log.warn("응답 데이터 null");
        }
    }
}
