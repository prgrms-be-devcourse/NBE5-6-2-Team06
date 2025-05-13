package com.grepp.matnam.app.controller.web.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    @GetMapping
    public String userManagement(Model model) {
        model.addAttribute("pageTitle", "사용자 관리");
        model.addAttribute("currentPage", "user-management");

        // 여기에 사용자 관리 데이터를 모델에 추가
        // 예: 사용자 목록, 신고 목록 등

        return "admin/user-management";
    }

}