package com.grepp.matnam.app.controller.api.team;

import com.grepp.matnam.app.controller.api.team.payload.TeamRequest;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
@Slf4j
public class TeamApiController {

    private final TeamService teamService;
    private final UserService userService;

    // 모임 생성 API
    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody TeamRequest request) {
        User user = userService.getUserById(request.getUserId());
        Team team = request.getTeam();
        team.setUser(user);
        teamService.saveTeam(team);
        teamService.addParticipant(team.getTeamId(), user);
        return ResponseEntity.ok("Team created successfully");
    }


    // 모임 상세 조회 API
    @GetMapping("/detail/{teamId}")
    public ResponseEntity<Team> getTeamDetail(@PathVariable Long teamId) {
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team);
    }

    // 참여자 목록 조회 API
    @GetMapping("/participants/{teamId}")
    public ResponseEntity<List<Participant>> getParticipants(@PathVariable Long teamId) {
        // 팀 ID에 해당하는 모든 참여자를 가져옴
        List<Participant> participants = teamService.getParticipant(teamId);

        // 참여자 목록을 반환
        return ResponseEntity.ok(participants);
    }


    // 모임 참여 신청 API
    @PostMapping("/{teamId}/apply")
    public ResponseEntity<String> applyToJoinTeam(@PathVariable Long teamId, @RequestParam String userId) {
        User user = userService.getUserById(userId);
        teamService.addParticipant(teamId, user);
        return ResponseEntity.ok("User applied to team");
    }

    // 모임 참여 수락 API
    @PostMapping("/participants/{teamId}/{userId}")
    public ResponseEntity<String> acceptParticipant(@PathVariable Long teamId, @PathVariable String userId) {
        teamService.acceptParticipant(teamId, userId);
        return ResponseEntity.ok("Participant accepted");
    }

    @PutMapping("/{teamId}/complete")
    public ResponseEntity<String> completeTeam(@PathVariable Long teamId) {
        try {
            teamService.completeTeam(teamId);
            return ResponseEntity.ok("모임이 성공적으로 완료 처리되었습니다.");
        } catch (Exception e) {
            log.error("모임 완료 처리 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body("모임 완료 처리에 실패했습니다: " + e.getMessage());
        }
    }
}

