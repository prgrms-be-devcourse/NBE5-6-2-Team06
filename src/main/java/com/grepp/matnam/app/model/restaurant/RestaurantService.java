package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
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

}
