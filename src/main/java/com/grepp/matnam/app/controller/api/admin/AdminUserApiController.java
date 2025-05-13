package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRequest;
import com.grepp.matnam.app.controller.api.admin.payload.UserStatusRequest;
import com.grepp.matnam.app.model.restaurant.RestaurantService;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.code.Status;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user")
@Slf4j
@RequiredArgsConstructor
public class AdminUserApiController {
    private final UserService userService;

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUserStatus(@PathVariable String userId,
        @RequestBody UserStatusRequest request) {

        userService.updateUserStatus(userId, request);
        return ResponseEntity.ok("사용자 상태가 수정되었습니다.");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long restaurantId) {
//        restaurantService.deleteById(restaurantId);
        return ResponseEntity.ok("사용자가 삭제되었습니다.");
    }

}
