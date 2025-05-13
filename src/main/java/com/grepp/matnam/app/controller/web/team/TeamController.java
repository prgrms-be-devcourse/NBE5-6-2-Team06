package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/team")
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
        @RequestParam("userId") String userId) {
        User user = teamService.getUserById(userId);
        team.setUser(user);

        teamService.saveTeam(team);
        teamService.addParticipant(team.getTeamId(), user);

        return "redirect:/team/search";
    }

    // 모임 상세 조회
    @GetMapping("/detail/{teamId}")
    public String getTeamDetail(@PathVariable Long teamId, Model model) {
        Team team = teamService.getTeamById(teamId);
        model.addAttribute("team", team);
        return "team/teamDetail";
    }

    // 모임 수정
    @PatchMapping("/detail/{teamId}")
    public String updateTeam(@PathVariable Long teamId, @ModelAttribute Team team) {
        team.setTeamId(teamId);
        teamService.saveTeam(team);
        return "redirect:/team/{teamId}/detail" + teamId;
    }

    // 모임 삭제
    @DeleteMapping("/detail/{teamId}")
    public String deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return "redirect:/team/search";
    }

    // 모임 상태 변경
    @PatchMapping("/{teamId}/status")
    public String changeTeamStatus(@PathVariable Long teamId, @RequestParam Status status) {
        teamService.changeTeamStatus(teamId, status);
        return "redirect:/team/{teamId}/detail" + teamId;
    }


    // 모임 참여 신청
    @PostMapping("/{teamId}/apply")
    public String applyToJoinTeam(@PathVariable Long teamId, @RequestParam String userId) {
        User user = teamService.getUserById(userId);
        teamService.addParticipant(teamId, user);
        return "redirect:/team/{teamId}/detail" + teamId;
    }


    // 모임 참여 수락 (주최자가 호출)
    @PostMapping("/participants/{teamId}/{userId}")
    public ResponseEntity<String> acceptParticipant(
        @PathVariable Long teamId,
        @PathVariable String userId) {

        try {
            teamService.acceptParticipant(teamId, userId);
            return ResponseEntity.ok("참가자가 수락되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("참가자 수락에 실패했습니다.");
        }
    }

    // 참여자 목록 조회(팀 페이지?)
    @GetMapping("/{teamId}/participants")
    public String getParticipants(@PathVariable Long teamId, Model model) {
        Team team = teamService.getTeamById(teamId);
        model.addAttribute("participants", team.getParticipants());
        return "team/teamPage";
    }

    // 참여자 상태 변경
    @PatchMapping("/{participantId}/participantStatus")
    public String changeParticipantStatus(@PathVariable Long participantId, @RequestParam
    ParticipantStatus status) {
        teamService.changeParticipantStatus(participantId, status);
        return "redirect:/team/{teamId}/detail" + participantId;
    }

    // 전체 모임 조회
    @GetMapping("/all/{userId}")
    public String getAllTeams(@PathVariable String userId,
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
        return "user/mypage";
    }

}
