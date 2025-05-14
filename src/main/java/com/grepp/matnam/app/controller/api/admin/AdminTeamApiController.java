package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.admin.payload.ParticipantResponse;
import com.grepp.matnam.app.controller.api.admin.payload.TeamResponse;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.infra.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/team")
@Slf4j
@RequiredArgsConstructor
public class AdminTeamApiController {

    private final TeamService teamService;

    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamDetail(@PathVariable Long teamId) {
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(ApiResponse.success(new TeamResponse(team)));
    }

    @GetMapping("/participant/{teamId}")
    public ResponseEntity<ApiResponse<List<ParticipantResponse>>> getParticipant(@PathVariable Long teamId) {
        List<Participant> participants = teamService.findAllWithUserByTeamId(teamId);
        List<ParticipantResponse> response = participants.stream()
            .map(ParticipantResponse::new)
            .toList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<?> updateTeamStatus(@PathVariable Long teamId, @RequestBody Status status) {
        teamService.updateTeamStatus(teamId, status);
        return ResponseEntity.ok("모임 상태가 변경되었습니다.");
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> unActivatedTeam(@PathVariable Long teamId) {
        teamService.unActivatedById(teamId);
        return ResponseEntity.ok("모임이 비활성화되었습니다.");
    }

}
