package com.grepp.matnam.app.controller.api.restaurant;

import com.grepp.matnam.app.controller.api.restaurant.payload.RestaurantRecommendResponse;

import com.grepp.matnam.app.model.restaurant.RestaurantAiService;
import com.grepp.matnam.app.model.restaurant.RestaurantService;
import com.grepp.matnam.app.model.restaurant.dto.RestaurantDto;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "LLM 연결 테스트", description = "메세지를 통해 LLM 연결 테스트합니다.")
    public ApiResponse<String> chat(String message) {
        try {
            return ApiResponse.success(restaurantAiService.chat(message));
        } catch (Exception e) {
            log.error("LLM 오류", e);
            return ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("recommend/restaurant/{teamId}")
    @Operation(summary = "추천", description = "팀에 속한 사용자의 취향 키워드를 종합하여 이를 기반으로 추천을 제공합니다.")
    public ApiResponse<RestaurantRecommendResponse> recommend(@PathVariable Long teamId) {
        try {
            List<String> keywords = teamService.countPreferenceKeyword(teamId);
            log.info("teamId {}에 대한 추출된 키워드: {}", teamId, keywords);

            String keywordPrompt = "우리 팀은 다음 키워드를 선호해요: " +
                String.join(", ", keywords) + ". 이 키워드를 기반으로 최적의 식당 3곳을 추천해줘.";
            log.info("keywordPrompt {}", keywordPrompt);

            RestaurantRecommendResponse response = restaurantAiService.recommendRestaurant(keywordPrompt);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("추천 처리 중 오류 발생", e);
            return ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 재추천
    @GetMapping("reRecommend/restaurant")
    @Operation(summary = "재추천", description = "취향 기반 추천이 마음에 들지 않을 때 제공하는 재추천입니다.")
    public ApiResponse<RestaurantRecommendResponse> reRecommend() {
        try {
            return ApiResponse.success(restaurantAiService.reRecommendRestaurant());
        } catch (Exception e) {
            log.error("재추천 오류 발생", e);
            return ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    //식당 정보 불러오기
    @GetMapping("/restaurant/name")
    @Operation(summary = "식당 정보 조회", description = "사용자에게 전달하기 위해 식당 정보 조회입니다.")
    public ApiResponse<RestaurantDto> getRestaurantByName(@RequestParam String name) {
        try {
            RestaurantDto dto = restaurantService.findByName(name);
            if (dto == null) {
                return ApiResponse.error(ResponseCode.NOT_FOUND);
            }
            return ApiResponse.success(dto);
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 요청 파라미터: {}", name);
            return ApiResponse.error(ResponseCode.BAD_REQUEST);
        } catch (Exception e) {
            log.error("식당 정보 조회 실패", e);
            return ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
