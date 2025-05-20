package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRankingResponse;
import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRequest;
import com.grepp.matnam.app.model.restaurant.dto.RestaurantDto;
import com.grepp.matnam.app.controller.web.admin.payload.RestaurantStatsResponse;
import com.grepp.matnam.app.model.restaurant.code.Category;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;


    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Page<Restaurant> findPaged(Pageable pageable) {
        return restaurantRepository.findPaged(pageable);
    }

    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Transactional
    public void updateRestaurant(Long restaurantId,  RestaurantRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new IllegalArgumentException("해당 식당이 존재하지 않습니다."));

        restaurant.setName(request.getName());
        restaurant.setCategory(request.getCategory());
        restaurant.setAddress(request.getAddress());
        restaurant.setTel(request.getTel());
        restaurant.setOpenTime(request.getOpenTime());
        restaurant.setMainFood(request.getMainFood());
        restaurant.setSummary(request.getSummary());
        restaurant.setGoodTalk(request.isGoodTalk());
        restaurant.setManyDrink(request.isManyDrink());
        restaurant.setGoodMusic(request.isGoodMusic());
        restaurant.setClean(request.isClean());
        restaurant.setGoodView(request.isGoodView());
        restaurant.setTerrace(request.isTerrace());
        restaurant.setGoodPicture(request.isGoodPicture());
        restaurant.setGoodMenu(request.isGoodMenu());
        restaurant.setLongStay(request.isLongStay());
        restaurant.setBigStore(request.isBigStore());
        restaurant.setGoogleRating(request.getGoogleRating());
    }

    @Transactional
    public void unActivatedRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new IllegalArgumentException("식당을 찾을 수 없습니다."));
        restaurant.unActivated();
    }

    @Transactional
    public void createRestaurant(RestaurantRequest request) {
        restaurantRepository.save(request.toEntity());
    }

    public Page<Restaurant> findByFilter(String category, String keyword, Pageable pageable) {
        if (StringUtils.hasText(category) && StringUtils.hasText(keyword)) {
            return restaurantRepository.findByCategoryAndNameContaining(category, keyword, pageable);
        } else if (StringUtils.hasText(category)) {
            return restaurantRepository.findByCategory(category, pageable);
        } else if (StringUtils.hasText(keyword)) {
            return restaurantRepository.findByNameContaining(keyword, pageable);
        } else {
            return restaurantRepository.findAll(pageable);
        }
    }

    public RestaurantDto findByName(String name) {
        Restaurant restaurant = restaurantRepository.findByName(name)
            .orElse(null);

        if (restaurant == null) {
            throw new RuntimeException("식당 없음");
        }

        return new RestaurantDto(
            restaurant.getName(),
            restaurant.getSummary(),
            restaurant.getAddress(),
            restaurant.getCategory(),
            restaurant.getOpenTime(),
            restaurant.getMainFood()
        );
    }

    public Map<String, Long> getRestaurantCategoryDistribution() {
        Map<String, Long> distribution = new HashMap<>();
        for (Category category : Category.values()) {
            long count = restaurantRepository.countByCategoryAndActivatedTrue(category.getKoreanName());
            distribution.put(category.getKoreanName(), count);
        }
        return distribution;
    }

    public Map<String, Long> getRestaurantMoodPreference() {
        Map<String, Long> moodCounts = new HashMap<>();
        moodCounts.put("대화", restaurantRepository.countByGoodTalkAndActivatedTrue(true));
        moodCounts.put("다양한 술", restaurantRepository.countByManyDrinkAndActivatedTrue(true));
        moodCounts.put("좋은 음악", restaurantRepository.countByGoodMusicAndActivatedTrue(true));
        moodCounts.put("깨끗함", restaurantRepository.countByCleanAndActivatedTrue(true));
        moodCounts.put("좋은 뷰", restaurantRepository.countByGoodViewAndActivatedTrue(true));
        moodCounts.put("테라스", restaurantRepository.countByIsTerraceAndActivatedTrue(true));
        moodCounts.put("사진", restaurantRepository.countByGoodPictureAndActivatedTrue(true));
        moodCounts.put("다양한 메뉴", restaurantRepository.countByGoodMenuAndActivatedTrue(true));
        moodCounts.put("오래 머물기", restaurantRepository.countByLongStayAndActivatedTrue(true));
        moodCounts.put("넓은 매장", restaurantRepository.countByBigStoreAndActivatedTrue(true));
        return moodCounts;
    }

    public List<RestaurantRankingResponse> getTop5RecommendedRestaurants() {
        Pageable top10 = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "recommendedCount"));
        List<Restaurant> topRestaurants = restaurantRepository.findAllByActivatedTrue(top10).getContent();
        return topRestaurants.stream()
            .map(restaurant -> new RestaurantRankingResponse(restaurant.getName(), restaurant.getRecommendedCount()))
            .collect(Collectors.toList());
    }

    public RestaurantStatsResponse getConsolidatedRestaurantStatistics() {
        RestaurantStatsResponse statsResponse = new RestaurantStatsResponse();

        // 전체 통계
        statsResponse.setTotalRestaurants(restaurantRepository.countByActivatedTrue());
        statsResponse.setAverageGoogleRating(restaurantRepository.averageGoogleRating());
        statsResponse.setTotalRecommendedCount(restaurantRepository.sumRecommendedCount());

        // 카테고리별 통계
        Map<String, Long> categoryRestaurantCounts = new HashMap<>();
        Map<String, Double> categoryAverageRatings = new HashMap<>();
        Map<String, Long> categoryTotalRecommendedCounts = new HashMap<>();

        for (Category category : Category.values()) {
            long count = restaurantRepository.countByCategoryAndActivatedTrue(category.getKoreanName());
            Double avgRating = restaurantRepository.averageGoogleRatingByCategory(category.getKoreanName());
            Long totalRecommended = restaurantRepository.sumRecommendedCountByCategory(category.getKoreanName());

            categoryRestaurantCounts.put(category.name(), count);
            categoryAverageRatings.put(category.name(), avgRating != null ? avgRating : 0.0);
            categoryTotalRecommendedCounts.put(category.name(), totalRecommended != null ? totalRecommended : 0L);
        }

        statsResponse.setCategoryRestaurantCounts(categoryRestaurantCounts);
        statsResponse.setCategoryAverageRatings(categoryAverageRatings);
        statsResponse.setCategoryTotalRecommendedCounts(categoryTotalRecommendedCounts);

        return statsResponse;
    }
}
