package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRequest;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import com.grepp.matnam.infra.error.exceptions.CommonException;
import com.grepp.matnam.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));

        restaurant.setName(request.getName());
        restaurant.setCategory(request.getCategory());
        restaurant.setAddress(request.getAddress());
        restaurant.setTel(request.getTel());
        restaurant.setOpenTime(request.getOpenTime());
        restaurant.setMainFood(request.getMainFood());
        restaurant.setSummary(request.getSummary());
        restaurant.setMood(request.getMood());
        restaurant.setGoogleRating(request.getGoogleRating());
        restaurant.setNaverRating(request.getNaverRating());
        restaurant.setKakaoRating(request.getKakaoRating());
    }
}
