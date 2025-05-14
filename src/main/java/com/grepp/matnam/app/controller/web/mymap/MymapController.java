package com.grepp.matnam.app.controller.web.mymap;

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
