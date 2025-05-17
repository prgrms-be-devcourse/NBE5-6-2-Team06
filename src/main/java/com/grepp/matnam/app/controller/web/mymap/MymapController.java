package com.grepp.matnam.app.controller.web.mymap;

import com.grepp.matnam.app.model.mymap.MymapService;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MymapController {

    private final UserService userService;
    private final MymapService mymapService;

    @GetMapping("/map/add")
    public String showAddPage() {
        return "map/add";
    }

    @GetMapping("/map/editMap")
    public String showEditMapPage(Model model) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);

        Map<String, Long> placeCounts = mymapService.getPlaceCounts(user);
        model.addAttribute("visiblePlaceCount", placeCounts.get("visible"));
        model.addAttribute("hiddenPlaceCount", placeCounts.get("hidden"));

        return "map/editMap";
    }
}