package com.grepp.matnam.app.controller.web.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admin/notification")
@RequiredArgsConstructor
public class AdminNotificationController {

    @GetMapping
    public String notification(Model model) {
        model.addAttribute("pageTitle", "알림 관리");
        model.addAttribute("currentPage", "notification");

        // 여기에 알림 데이터를 모델에 추가
        // 예: 알림 목록 등

        return "admin/notification";
    }

}