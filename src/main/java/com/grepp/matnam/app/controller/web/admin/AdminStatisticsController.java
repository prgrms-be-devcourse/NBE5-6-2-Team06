package com.grepp.matnam.app.controller.web.admin;

import com.grepp.matnam.app.controller.web.admin.payload.RestaurantStatsResponse;
import com.grepp.matnam.app.controller.web.admin.payload.TeamStatsResponse;
import com.grepp.matnam.app.controller.web.admin.payload.UserStatsResponse;
import com.grepp.matnam.app.model.restaurant.RestaurantService;
import com.grepp.matnam.app.model.team.TeamService;
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
    private final TeamService teamService;
    private final RestaurantService restaurantService;

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
        TeamStatsResponse teamStats = teamService.getTeamStatistics();

        model.addAttribute("teamStats", teamStats);
        model.addAttribute("activeTab", "team-stats");
        model.addAttribute("pageTitle", "통계 및 분석");
        model.addAttribute("currentPage", "statistics");

        return "admin/statistics";
    }

    @GetMapping("/restaurant")
    public String restaurantStatistics(Model model) {
        RestaurantStatsResponse restaurantStats = restaurantService.getConsolidatedRestaurantStatistics();

        model.addAttribute("activeTab", "restaurant-stats");
        model.addAttribute("pageTitle", "통계 및 분석");
        model.addAttribute("currentPage", "statistics");
        model.addAttribute("restaurantStats", restaurantStats);

        return "admin/statistics";
    }

}