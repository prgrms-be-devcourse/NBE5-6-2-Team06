package com.grepp.matnam.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MymapController {

    @GetMapping("/map/add")
    public String showAddPage() {
        return "map/add";
    }
}
