package com.grepp.matnam.app.controller.web.admin;

import com.grepp.matnam.app.model.restaurant.RestaurantService;
import com.grepp.matnam.app.model.restaurant.code.Category;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import com.grepp.matnam.infra.error.exceptions.CommonException;
import com.grepp.matnam.infra.payload.PageParam;
import com.grepp.matnam.infra.response.PageResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import jakarta.validation.Valid;
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
@RequestMapping("/admin/restaurant")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public String restaurantManagement(@RequestParam(required = false) Category category,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false, defaultValue = "newest") String sort,
        @Valid PageParam param, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
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

        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize(), sortOption);

        String categoryKoreanName = "";
        String categoryName = "";
        if (category != null) {
            categoryKoreanName = category.getKoreanName();
            categoryName = category.name();
        }
        Page<Restaurant> page = restaurantService.findByFilter(categoryKoreanName, keyword,
            pageable);

        if (param.getPage() != 1 && page.getContent().isEmpty()) {
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

}