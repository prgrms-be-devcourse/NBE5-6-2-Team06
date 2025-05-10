package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.model.team.entity.Team;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("team")
public class TeamController {

    private TeamService teamService;

    @GetMapping("/search")
    public String search() {
        return "team/teamSearch";
    }

    @GetMapping("/detail")
    public String detail(){
        return "team/teamDetail";
    }

    @GetMapping("/create")
    public String create(){
        return "team/teamCreate";
    }

    @PostMapping("/create")
    public String createTeam(@ModelAttribute Team team,
        @RequestParam("image") MultipartFile image){

        String imageUrl = teamService.uploadImage(image);
        team.setImageUrl(imageUrl);

        teamService.saveTeam(team);
        return "redirect:/team";
    }

    @GetMapping("/get-location")
    public String getLocation(@RequestParam("address") String address, Model model) {

        return "location";
    }
}
