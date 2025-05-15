package com.grepp.matnam.app.controller.web.admin;

import com.grepp.matnam.app.controller.web.admin.payload.NewTeamResponse;
import com.grepp.matnam.app.controller.web.admin.payload.TotalUserResponse;
import com.grepp.matnam.app.controller.web.admin.payload.UserActivityLogResponse;
import com.grepp.matnam.app.model.log.UserActivityLogService;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')") // TODO : 모든 관리자 페이지 권한 설정 필요
public class AdminDashboardController {

    private final UserActivityLogService userActivityLogService;
    private final UserService userService;
    private final TeamService teamService;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {

        UserActivityLogResponse todayLogStats = userActivityLogService.getTodayLogStats();
        TotalUserResponse totalUserStats = userService.getTotalUserStats();
        NewTeamResponse newTeamStats = teamService.getNewTeamStats();

        model.addAttribute("pageTitle", "대시보드");
        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("totalUserStats", totalUserStats);
        model.addAttribute("todayLogStats", todayLogStats);
        model.addAttribute("newTeamStats", newTeamStats);

        return "admin/dashboard";
    }

}