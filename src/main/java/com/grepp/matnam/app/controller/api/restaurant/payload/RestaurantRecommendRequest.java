package com.grepp.matnam.app.controller.api.restaurant.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRecommendRequest {
    // LLM 넘겨줄 데이터 정보 담는 입력용 객체

    private List<String> teamKeywords; // 집계된 키워드
//    private List<Restaurant> restaurants; // 식당 리스트

}
