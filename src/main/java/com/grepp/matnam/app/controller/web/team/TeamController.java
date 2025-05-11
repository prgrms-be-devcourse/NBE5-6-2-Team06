package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/team")
@RequiredArgsConstructor
@Slf4j
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/search")
    public String search() {
        return "team/teamSearch";
    }

    @GetMapping("/create")
    public String create() {
        return "team/teamCreate";
    }

    // 모임 생성
    @PostMapping
    public String createTeam(@ModelAttribute Team team,
        @RequestParam("userId") Long userId
    ) {
        User user = teamService.getUserById(userId);
        team.setUser(user);

        teamService.saveTeam(team);
        teamService.addParticipant(team.getTeamId(), user);

        return "redirect:/team";
    }

    // 모임 상세 조회
    @GetMapping("/{teamId}")
    public String getTeamDetail(@PathVariable Long teamId, Model model) {
        Team team = teamService.getTeamById(teamId);
        model.addAttribute("team", team);
        return "team/teamDetail";
    }

    // 모임 수정
    @PatchMapping("/{teamId}")
    public String updateTeam(@PathVariable Long teamId, @ModelAttribute Team team) {
        team.setTeamId(teamId);
        teamService.saveTeam(team);
        return "redirect:/teamPage/" + teamId;
    }

    // 모임 삭제
    @DeleteMapping("/{teamId}")
    public String deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return "redirect:/teamSearch";
    }

    // 모임 상태 변경
    @PatchMapping("/{teamId}/status")
    public String changeTeamStatus(@PathVariable Long teamId, @RequestParam Status status) {
        teamService.changeTeamStatus(teamId, status);
        return "redirect:/teamPage/" + teamId;
    }


    // 모임 참여 신청
    @PostMapping("/{teamId}/apply")
    public String applyToJoinTeam(@PathVariable Long teamId, @RequestParam Long userId) {
        User user = teamService.getUserById(userId);
        teamService.addParticipant(teamId, user);
        return "redirect:/teamDetail/" + teamId;
    }


    // 모임 참여 수락
    @PostMapping("/{teamId}/accept")
    public String acceptParticipant(@PathVariable Long teamId, @RequestParam Long userId) {
        teamService.acceptParticipant(teamId, userId);
        return "redirect:/team/" + teamId;
    }

    // 참가자 목록 조회(팀 페이지?)
    @GetMapping("/{teamId}/participants")
    public String getParticipants(@PathVariable Long teamId, Model model) {
        Team team = teamService.getTeamById(teamId);
        model.addAttribute("participants", team.getParticipants());
        return "team/teamPage";
    }

    // 참가자 상태 변경
    @PatchMapping("/{participantId}/participantStatus")
    public String changeParticipantStatus(@PathVariable Long participantId, @RequestParam
    ParticipantStatus status) {
        teamService.changeParticipantStatus(participantId, status);
        return "redirect:/team/" + participantId;
    }

    // 전체 모임 조회
    @GetMapping("/all/{userId}")
    public String getAllTeams(@PathVariable Long userId,
        @RequestParam(value = "status", required = false) String status,
        Model model) {

        Iterable<Team> teams;

        // 주최 중인 모임 조회
        if ("LEADER".equals(status)) {
            teams = teamService.getTeamsByLeader(userId);
        }
        // 참여 중인 모임 조회
        else if ("MEMBER".equals(status)) {
            teams = teamService.getTeamsByParticipant(userId);
        }
        // 전체 모임 조회
        else {
            teams = teamService.getUserTeams(userId);
        }

        model.addAttribute("teams", teams);
        return "member/mypage";
    }

    // 사용자가 주최한 모임 조회
    @GetMapping("/{userId}")
    public String getUserTeams(@PathVariable Long userId, Model model) {
        Iterable<Team> teams = teamService.getUserTeams(userId);
        model.addAttribute("teams", teams);
        return "member/mypage";
    }

}
