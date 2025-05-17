package com.grepp.matnam.app.controller.web;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.entity.RankingItem;
import com.grepp.matnam.app.model.content.service.ContentRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ContentRankingService contentRankingService;

    @GetMapping("/")
    public String index(Model model) {
        Optional<ContentRanking> activeRanking = contentRankingService.getCurrentActiveRanking();

        // 랭킹 가져오기
        if (activeRanking.isPresent()) {
            ContentRanking ranking = activeRanking.get();
            model.addAttribute("contentRanking", ranking);

            List<RankingItem> items = contentRankingService.getActiveRankingItems(ranking);
            if (!items.isEmpty()) {
                model.addAttribute("rankingItems", items);
            }
        }

        return "index";
    }
}