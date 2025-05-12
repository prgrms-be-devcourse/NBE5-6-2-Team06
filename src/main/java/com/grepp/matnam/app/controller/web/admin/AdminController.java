package com.grepp.matnam.app.controller.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "대시보드");
        model.addAttribute("currentPage", "dashboard");

        // 여기에 대시보드 데이터를 모델에 추가
        // 예: 총 회원 수, 오늘 서비스 이용 인원 등

        return "admin/dashboard";
    }

    @GetMapping("/user-management")
    public String userManagement(Model model) {
        model.addAttribute("pageTitle", "사용자 관리");
        model.addAttribute("currentPage", "user-management");

        // 여기에 사용자 관리 데이터를 모델에 추가
        // 예: 사용자 목록, 신고 목록 등

        return "admin/user-management";
    }

    @GetMapping("/team-management")
    public String teamManagement(Model model) {
        model.addAttribute("pageTitle", "모임 관리");
        model.addAttribute("currentPage", "team-management");

        // 여기에 모임 관리 데이터를 모델에 추가
        // 예: 모임 목록 등

        return "admin/team-management";
    }

    @GetMapping("/restaurant-management")
    public String restaurantManagement(Model model) {
        model.addAttribute("pageTitle", "식당 및 메뉴 관리");
        model.addAttribute("currentPage", "restaurant-management");

        // 여기에 식당 관리 데이터를 모델에 추가
        // 예: 식당 목록, 카테고리 목록 등

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