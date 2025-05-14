package com.grepp.matnam.app.controller.api.team;

import com.grepp.matnam.app.controller.api.team.payload.TeamRequest;
import com.grepp.matnam.app.controller.api.team.payload.TeamResponse;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

//    // 모임 생성 API
//    @PostMapping("/create")
//    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamRequest request) {
//        User user = userService.getUserById(request.getUserId());
//        Team team = request.getTeam();
//        team.setUser(user);
//
//        teamService.saveTeam(team);
//        teamService.addParticipant(team.getTeamId(), user);
//
//        TeamResponse teamResponse = new TeamResponse(team);
//        return ResponseEntity.ok(teamResponse);
//    }



//    // 모임 상세 조회 API
//    @GetMapping("/detail/{teamId}")
//    public ResponseEntity<Team> getTeamDetail(@PathVariable Long teamId) {
//        Team team = teamService.getTeamByIdWithParticipants(teamId);
//        return ResponseEntity.ok(team);
//    }
//
//    // 참여자 목록 조회 API
//    @GetMapping("/participants/{teamId}")
//    public ResponseEntity<List<Participant>> getParticipants(@PathVariable Long teamId) {
//        List<Participant> participants = teamService.getParticipant(teamId);
//
//        return ResponseEntity.ok(participants);
//    }


//    // 모임 참여 신청 API
//    @PostMapping("/{teamId}/apply")
//    public ResponseEntity<String> applyToJoinTeam(@PathVariable Long teamId, @RequestParam String userId) {
//        User user = userService.getUserById(userId);
//        teamService.addParticipant(teamId, user);
//        return ResponseEntity.ok("User applied to team");
//    }
//
//    // 모임 참여 수락 API
//    @PostMapping("/participants/{teamId}/{userId}")
//    public ResponseEntity<String> acceptParticipant(@PathVariable Long teamId, @PathVariable String userId) {
//        teamService.acceptParticipant(teamId, userId);
//        return ResponseEntity.ok("Participant accepted");
//    }

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

    // 모임 수정
    @PatchMapping("/detail/{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable Long teamId, @RequestBody Team team) {
        team.setTeamId(teamId);
        teamService.saveTeam(team);
        return ResponseEntity.ok().build();
    }

    // 모임 삭제
    @DeleteMapping("/detail/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build(); // 성공적인 삭제, 응답 본문 없음
    }

    // 모임 상태 변경
    @PatchMapping("/{teamId}/status")
    public ResponseEntity<?> changeTeamStatus(@PathVariable Long teamId, @RequestParam Status status) {
        teamService.changeTeamStatus(teamId, status);
        return ResponseEntity.ok().build();
    }

    // 참여자 상태 변경
    @PatchMapping("/{participantId}/participantStatus")
    public ResponseEntity<?> changeParticipantStatus(@PathVariable Long participantId, @RequestParam ParticipantStatus status) {
        teamService.changeParticipantStatus(participantId, status);
        return ResponseEntity.ok().build();
    }
}

