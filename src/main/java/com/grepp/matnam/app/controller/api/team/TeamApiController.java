package com.grepp.matnam.app.controller.api.team;

import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        Team existingTeam = teamService.getTeamById(teamId);
        if (existingTeam == null) {
            return ResponseEntity.notFound().build();
        }
        team.setTeamId(teamId);
        team.setUser(existingTeam.getUser());

        teamService.saveTeam(team);
        return ResponseEntity.ok().build();
    }

    // 모임 삭제
    @DeleteMapping("/detail/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }

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
}

