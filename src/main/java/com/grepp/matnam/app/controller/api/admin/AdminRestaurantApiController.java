package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRequest;
import com.grepp.matnam.app.controller.api.admin.validator.RestaurantRequestValidator;
import com.grepp.matnam.app.model.restaurant.RestaurantService;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import com.grepp.matnam.infra.error.exceptions.CommonException;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
public class AdminRestaurantApiController {

    private final RestaurantService restaurantService;

    @InitBinder("restaurantRequest")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new RestaurantRequestValidator());
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<ApiResponse<Restaurant>> getRestaurant(@PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId)
            .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));
        log.info("{}", restaurant);
        return ResponseEntity.ok(ApiResponse.success(restaurant));
    }

    @PatchMapping("/{restaurantId}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long restaurantId,
        @RequestBody @Valid RestaurantRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        restaurantService.updateRestaurant(restaurantId, request);
        return ResponseEntity.ok("식당이 수정되었습니다.");
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<?> unActivatedRestaurant(@PathVariable Long restaurantId) {
        restaurantService.unActivatedRestaurant(restaurantId);
        return ResponseEntity.ok("식당이 삭제되었습니다.");
    }

    @PostMapping
    public ResponseEntity<?> createRestaurant(@RequestBody @Valid RestaurantRequest request,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        restaurantService.createRestaurant(request);
        return ResponseEntity.ok("식당이 추가되었습니다.");
    }
}
