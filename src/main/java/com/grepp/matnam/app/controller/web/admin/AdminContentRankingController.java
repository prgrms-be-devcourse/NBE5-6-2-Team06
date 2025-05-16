package com.grepp.matnam.app.controller.web.admin;

import com.grepp.matnam.app.model.content.service.ContentRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/content-rankings")
@RequiredArgsConstructor
public class AdminContentRankingController {

    private final ContentRankingService contentRankingService;

    @GetMapping
    public String listRankings(Model model) {
        model.addAttribute("rankings", contentRankingService.getAllRankings());
        model.addAttribute("currentPage", "content-rankings");
        model.addAttribute("pageTitle", "콘텐츠 랭킹 관리");

        return "admin/content-rankings";
    }
}