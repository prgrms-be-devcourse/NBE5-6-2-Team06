package com.grepp.matnam.app.restaurants;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.grepp.matnam.app.controller.api.restaurant.payload.RestaurantRecommendRequest;
import com.grepp.matnam.app.controller.api.restaurant.payload.RestaurantRecommendResponse;
import com.grepp.matnam.app.model.restaurant.RestaurantAiService;
import com.grepp.matnam.app.model.team.TeamService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class RecommendationTest {

    @Autowired
    private RestaurantAiService restaurantAiService;

    @Test
    public void testRecommend() {

        RestaurantRecommendResponse response = restaurantAiService.reRecommendRestaurant();

        // null 검사
        assertNotNull(response);
        log.info("식당 리스트: {}", response.restaurants());
        log.info("이유: {}", response.reason());
    }
}
