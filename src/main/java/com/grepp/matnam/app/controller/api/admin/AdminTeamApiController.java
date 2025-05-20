package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.admin.payload.ParticipantResponse;
import com.grepp.matnam.app.controller.api.admin.payload.TeamResponse;
import com.grepp.matnam.app.controller.api.admin.payload.TeamStatusUpdateRequest;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.infra.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
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
@Tag(name = "Admin Team API", description = "관리자 - 모임 관리 API")
public class AdminTeamApiController {

    private final TeamService teamService;

    @GetMapping("/{teamId}")
    @Operation(summary = "모임 상세 조회", description = "특정 모임의 상세를 조회합니다.")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamDetail(@PathVariable Long teamId) {
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(ApiResponse.success(new TeamResponse(team)));
    }

    @GetMapping("/participant/{teamId}")
    @Operation(summary = "모임 참가자 조회", description = "특정 모임의 참가자를 조회합니다.")
    public ResponseEntity<ApiResponse<List<ParticipantResponse>>> getParticipant(@PathVariable Long teamId) {
        List<Participant> participants = teamService.findAllWithUserByTeamId(teamId);
        List<ParticipantResponse> response = participants.stream()
            .map(ParticipantResponse::new)
            .toList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{teamId}")
    @Operation(summary = "모임 상세 변경", description = "특정 모임 상태를 변경합니다.")
    public ResponseEntity<?> updateTeamStatus(@PathVariable Long teamId, @RequestBody TeamStatusUpdateRequest teamStatusUpdateRequest) {
        teamService.updateTeamStatus(teamId, teamStatusUpdateRequest);
        return ResponseEntity.ok("모임 상태가 변경되었습니다.");
    }

    @DeleteMapping("/{teamId}")
    @Operation(summary = "모임 비활성화", description = "특정 모임을 비활성화합니다.")
    public ResponseEntity<?> unActivatedTeam(@PathVariable Long teamId) {
        teamService.unActivatedById(teamId);
        return ResponseEntity.ok("모임이 비활성화되었습니다.");
    }

    @GetMapping("/statistics/success-rate/monthly")
    @Operation(summary = "최근 6개월 모임 성공률 조회", description = "최근 6개월간의 모임 성공률을 조회합니다.")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getMonthlyMeetingSuccessRateData() {
        return ResponseEntity.ok(ApiResponse.success(teamService.getMonthlyMeetingSuccessRate()));
    }

    @GetMapping("/statistics/participant-distribution")
    @Operation(summary = "모임별 참가 인원 조회", description = "모임별 참가 인원을 조회합니다.")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getTeamParticipantDistribution() {
        Map<String, Long> participantDistribution = teamService.getTeamParticipantDistribution();
        return ResponseEntity.ok(ApiResponse.success(participantDistribution));
    }

    @GetMapping("/statistics/daily-new-teams")
    @Operation(summary = "최근 7일 모임 생성 수 조회", description = "최근 7일간의 모임 생성 수를 조회합니다.")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getDailyNewTeamCounts() {
        Map<String, Long> dailyNewTeamCounts = teamService.getDailyNewTeamCountsLast7Days();
        return ResponseEntity.ok(ApiResponse.success(dailyNewTeamCounts));
    }

}
