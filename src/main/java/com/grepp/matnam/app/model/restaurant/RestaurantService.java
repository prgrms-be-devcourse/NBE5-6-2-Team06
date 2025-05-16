package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRequest;
import com.grepp.matnam.app.model.restaurant.code.Category;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Map<String, Long> getRestaurantCategoryDistribution() {
        Map<String, Long> distribution = new HashMap<>();
        for (Category category : Category.values()) {
            long count = restaurantRepository.countByCategory(category.getKoreanName());
            distribution.put(category.getKoreanName(), count);
        }
        return distribution;
    }
}
