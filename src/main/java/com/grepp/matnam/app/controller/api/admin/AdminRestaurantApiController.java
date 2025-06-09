package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRankingResponse;
import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRequest;
import com.grepp.matnam.app.model.restaurant.RestaurantService;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import com.grepp.matnam.infra.error.exceptions.CommonException;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/restaurant")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Admin Restaurant API", description = "관리자 - 식당 관리 API")
public class AdminRestaurantApiController {

    private final RestaurantService restaurantService;

    @GetMapping("/{restaurantId}")
    @Operation(summary = "식당 상세 조회", description = "특정 식당의 상세를 조회합니다.")
    public ResponseEntity<ApiResponse<Restaurant>> getRestaurant(@PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId)
            .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));
        return ResponseEntity.ok(ApiResponse.success(restaurant));
    }

    @PatchMapping("/{restaurantId}")
    @Operation(summary = "식당 상세 수정", description = "특정 식당의 상세를 수정합니다.")
    public ResponseEntity<ApiResponse<Void>> updateRestaurant(@PathVariable Long restaurantId,
        @RequestBody @Valid RestaurantRequest request) {
        restaurantService.updateRestaurant(restaurantId, request);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @DeleteMapping("/{restaurantId}")
    @Operation(summary = "식당 비활성화", description = "특정 식당을 비활성화합니다.")
    public ResponseEntity<ApiResponse<Void>> unActivatedRestaurant(@PathVariable Long restaurantId) {
        restaurantService.unActivatedRestaurant(restaurantId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @PostMapping
    @Operation(summary = "새 식당 추가", description = "새 식당을 추가합니다.")
    public ResponseEntity<ApiResponse<Void>> createRestaurant(@RequestBody @Valid RestaurantRequest request) {
        restaurantService.createRestaurant(request);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @GetMapping("/statistics/category-distribution")
    @Operation(summary = "카테고리별 식당 수 조회", description = "카테고리별 식당 수를 조회합니다.")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getRestaurantCategoryDistribution() {
        Map<String, Long> categoryDistribution = restaurantService.getRestaurantCategoryDistribution();
        return ResponseEntity.ok(ApiResponse.success(categoryDistribution));
    }

    @GetMapping("/statistics/mood-preference")
    @Operation(summary = "식당 분위기 선호도 조회", description = "식당 분위기 선호도를 조회합니다.")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getRestaurantMoodPreference() {
        Map<String, Long> moodPreference = restaurantService.getRestaurantMoodPreference();
        return ResponseEntity.ok(ApiResponse.success(moodPreference));
    }

    @GetMapping("/statistics/top-recommended")
    @Operation(summary = "추천 누적 TOP 5 식당 조회", description = "추천 누적 TOP 5 식당들을 조회합니다.")
    public ResponseEntity<ApiResponse<List<RestaurantRankingResponse>>> getTop5Recommended() {
        List<RestaurantRankingResponse> top5Restaurants = restaurantService.getTop5RecommendedRestaurants();
        return ResponseEntity.ok(ApiResponse.success(top5Restaurants));
    }
}
