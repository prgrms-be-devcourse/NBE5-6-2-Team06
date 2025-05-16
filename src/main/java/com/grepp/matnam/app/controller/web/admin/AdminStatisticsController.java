package com.grepp.matnam.app.controller.web.admin;

import com.grepp.matnam.app.controller.web.admin.payload.UserStatsResponse;
import com.grepp.matnam.app.model.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admin/statistics")
@RequiredArgsConstructor
public class AdminStatisticsController {

    private final UserService userService;

    @GetMapping({"", "/", "/user"})
    public String userStatistics(Model model) {
        UserStatsResponse stats = userService.getUserStatistics();

        model.addAttribute("activeTab", "user-stats");
        model.addAttribute("pageTitle", "통계 및 분석");
        model.addAttribute("currentPage", "statistics");
        model.addAttribute("userStats", stats);

        return "admin/statistics";
    }

    @GetMapping("/team")
    public String teamStatistics(Model model) {
        model.addAttribute("activeTab", "team-stats");
        model.addAttribute("pageTitle", "통계 및 분석");
        model.addAttribute("currentPage", "statistics");

        // 여기에 통계 데이터를 모델에 추가
        // 예: 사용자 통계, 성공률 분석 등

        return "admin/statistics";
    }

    @GetMapping("/restaurant")
    public String restaurantStatistics(Model model) {
        model.addAttribute("activeTab", "restaurant-stats");
        model.addAttribute("pageTitle", "통계 및 분석");
        model.addAttribute("currentPage", "statistics");

        // 여기에 통계 데이터를 모델에 추가
        // 예: 사용자 통계, 성공률 분석 등

        return "admin/statistics";
    }

}