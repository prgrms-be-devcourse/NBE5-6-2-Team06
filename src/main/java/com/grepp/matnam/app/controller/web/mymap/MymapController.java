package com.grepp.matnam.app.controller.web.mymap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MymapController {

    // 맛집 등록 UI 페이지로 이동 (templates/map/add.html)
    @GetMapping("/map/add")
    public String showAddPage() {
        return "map/add";
    }
}