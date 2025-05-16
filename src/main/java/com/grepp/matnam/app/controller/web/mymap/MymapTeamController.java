package com.grepp.matnam.app.controller.web.mymap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MymapTeamController {

    @GetMapping("/team/map/{teamId}")
    public String showTeamMapPage(@PathVariable Long teamId, Model model) {
        model.addAttribute("teamId", teamId);
        return "map/teamMap";
    }
}