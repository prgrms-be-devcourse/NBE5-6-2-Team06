package com.grepp.matnam.app.controller.web.team;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("team")
public class TeamController {

    @GetMapping("/search")
    public String search() {return "team/teamSearch";}

    @GetMapping("/detail")
    public String detail(){return "team/teamDetail";}

    @GetMapping("/create")
    public String create(){return "team/teamCreate";}
}
