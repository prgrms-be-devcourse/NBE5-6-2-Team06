package com.grepp.matnam.app.controller.web.admin;

import com.grepp.matnam.app.model.restaurant.RestaurantService;
import com.grepp.matnam.app.model.restaurant.code.Category;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import com.grepp.matnam.infra.error.exceptions.CommonException;
import com.grepp.matnam.infra.payload.PageParam;
import com.grepp.matnam.infra.response.PageResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RestaurantService restaurantService;

    @GetMapping({"", "/","/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "대시보드");
        model.addAttribute("currentPage", "dashboard");

        // 여기에 대시보드 데이터를 모델에 추가
        // 예: 총 회원 수, 오늘 서비스 이용 인원 등

        return "admin/dashboard";
    }

    @GetMapping("/user")
    public String userManagement(Model model) {
        model.addAttribute("pageTitle", "사용자 관리");
        model.addAttribute("currentPage", "user-management");

        // 여기에 사용자 관리 데이터를 모델에 추가
        // 예: 사용자 목록, 신고 목록 등

        return "admin/user-management";
    }

    @GetMapping("/team")
    public String teamManagement(Model model) {
        model.addAttribute("pageTitle", "모임 관리");
        model.addAttribute("currentPage", "team-management");

        // 여기에 모임 관리 데이터를 모델에 추가
        // 예: 모임 목록 등

        return "admin/team-management";
    }

    @GetMapping("/restaurant")
    public String restaurantManagement(@RequestParam(required = false) Category category,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false, defaultValue = "newest") String sort,
        @Valid PageParam param, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        Sort sortOption;
        switch (sort) {
            case "name":
                sortOption = Sort.by("name").ascending();
                break;
            case "rating":
                sortOption = Sort.by(Sort.Order.desc("rating")); // 서비스에서 평균 계산 필드 필요
                break;
            default:
                sortOption = Sort.by(Sort.Order.desc("createdAt")); // 최신순
        }

        Pageable pageable = PageRequest.of(param.getPage()-1, param.getSize(), sortOption);

        String categoryKoreanName = "";
        String categoryName = "";
        if (category != null) {
            categoryKoreanName = category.getKoreanName();
            categoryName = category.name();
        }
        Page<Restaurant> page = restaurantService.findByFilter(categoryKoreanName, keyword, pageable);

        if (param.getPage() != 1 && page.getContent().isEmpty()){
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }
        PageResponse<Restaurant> response = new PageResponse<>("/admin/restaurant", page, 5);

        model.addAttribute("pageTitle", "식당 관리");
        model.addAttribute("currentPage", "restaurant-management");
        model.addAttribute("page", response);
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", categoryKoreanName);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("sort", sort);

        return "admin/restaurant-management";
    }

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("pageTitle", "통계 및 분석");
        model.addAttribute("currentPage", "statistics");

        // 여기에 통계 데이터를 모델에 추가
        // 예: 사용자 통계, 성공률 분석 등

        return "admin/statistics";
    }

    @GetMapping("/notification")
    public String notification(Model model) {
        model.addAttribute("pageTitle", "알림 관리");
        model.addAttribute("currentPage", "notification");

        // 여기에 알림 데이터를 모델에 추가
        // 예: 알림 목록 등

        return "admin/notification";
    }
}