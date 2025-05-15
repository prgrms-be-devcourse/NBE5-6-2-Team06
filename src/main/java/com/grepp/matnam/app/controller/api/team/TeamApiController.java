package com.grepp.matnam.app.controller.api.team;

import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.dto.TeamDto;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
//    @PutMapping("/update/{teamId}")
//    public ResponseEntity<String> updateTeam(@PathVariable Long teamId, @RequestBody TeamDto teamDto) {
//        try {
//            teamService.updateTeam(teamId, teamDto);
//            return ResponseEntity.ok("모임 수정 성공");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("모임 수정 실패");
//        }
//    }

//    // 모임 삭제
//    @DeleteMapping("/delete/{teamId}")
//    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId) {
//        try {
//            teamService.deleteTeam(teamId);
//            return ResponseEntity.ok("모임 삭제 성공");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("모임 삭제 실패");
//        }
//    }

    // 모임 상태 변경
    @PatchMapping("/{teamId}/status")
    public ResponseEntity<?> changeTeamStatus(@PathVariable Long teamId,
        @RequestParam Status status) {
        teamService.changeTeamStatus(teamId, status);
        return ResponseEntity.ok().build();
    }

    // 참여자 상태 변경
    @PatchMapping("/{participantId}/participantStatus")
    public ResponseEntity<?> changeParticipantStatus(@PathVariable Long participantId,
        @RequestParam ParticipantStatus status) {
        teamService.changeParticipantStatus(participantId, status);
        return ResponseEntity.ok().build();
    }

    // 참여 신청
    @PostMapping("/{teamId}/apply/{userId}")
    public ResponseEntity<String> applyToJoinTeam(@PathVariable Long teamId, @PathVariable String userId) {
        try {
            User user = userService.getUserById(userId);
            teamService.addParticipant(teamId, user);
            return ResponseEntity.ok("참여 신청 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("참여 신청 실패: " + e.getMessage());
        }
    }

    // 승인 처리
    @PostMapping("/{teamId}/approve/{participantId}")
    public ResponseEntity<Team> approveParticipant(@PathVariable Long teamId, @PathVariable Long participantId) {
        try {
            teamService.approveParticipant(participantId);
            Team team = teamService.getTeamById(teamId);
            return ResponseEntity.ok(team); // 팀 상태 반환
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

