package com.grepp.matnam.app.controller.api.restaurant;

import com.grepp.matnam.app.controller.api.restaurant.payload.RestaurantRecommendRequest;
import com.grepp.matnam.app.controller.api.restaurant.payload.RestaurantRecommendResponse;

import com.grepp.matnam.app.model.restaurant.RestaurantAiService;
import com.grepp.matnam.app.model.restaurant.RestaurantService;
import com.grepp.matnam.app.model.restaurant.dto.RestaurantDto;
import com.grepp.matnam.app.model.team.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/ai")
public class RestaurantApiController {

    private final RestaurantAiService restaurantAiService;
    private final TeamService teamService;
    private final RestaurantService restaurantService;

    // LLM 간단한 테스트 채팅 메시지[LLM 연결 확인용]
    @GetMapping("chat")
    public String chat(String message){return restaurantAiService.chat(message);}

    // 팀 맞춤 추천
    @GetMapping("recommend/restaurant/{teamId}")
    public RestaurantRecommendResponse recommend(@PathVariable Long teamId) {
        List<String> keywords = teamService.countPreferenceKeyword(teamId);
        RestaurantRecommendRequest request = new RestaurantRecommendRequest(keywords);
        System.out.println("넘어가는 keywords: " + keywords);

        return restaurantAiService.RecommendRestaurant(request);

    }

    // 재추천
    @GetMapping("reRecommend/restaurant")
    public RestaurantRecommendResponse reRecommend() {

        return restaurantAiService.reRecommendRestaurant();
    }

    //식당 정보 불러오기
    @GetMapping("/restaurant/name")
    public RestaurantDto getRestaurantByName(@RequestParam String name) {
        return restaurantService.findByName(name);
    }

}
