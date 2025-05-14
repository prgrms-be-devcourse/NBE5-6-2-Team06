package com.grepp.matnam.app.controller.web.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admin/team")
@RequiredArgsConstructor
public class AdminTeamController {

    @GetMapping
    public String teamManagement(Model model) {
        model.addAttribute("pageTitle", "모임 관리");
        model.addAttribute("currentPage", "team-management");

        // 여기에 모임 관리 데이터를 모델에 추가
        // 예: 모임 목록 등

        return "admin/team-management";
    }

}