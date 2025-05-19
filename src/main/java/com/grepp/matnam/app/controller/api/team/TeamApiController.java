package com.grepp.matnam.app.controller.api.team;

import com.grepp.matnam.app.controller.api.team.payload.TeamRequest;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.dto.TeamDto;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
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


    // 모임 상태 변경 - 모임 완료
    @PutMapping("/{teamId}/complete")
    public ResponseEntity<String> completeTeam(@PathVariable Long teamId, @RequestParam Status status) {

        String UserId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {

            teamService.completeTeam(teamId, status, UserId);
            return ResponseEntity.ok("모임이 성공적으로 완료 처리되었습니다.");
        } catch (Exception e) {
            log.error("모임 완료 처리 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body("모임 완료 처리에 실패했습니다: " + e.getMessage());
        }
    }

    // 모임 상태 변경 - 모임 취소
    @PostMapping("/{teamId}/cancel")
    public ResponseEntity<ApiResponse> cancelTeam(@PathVariable Long teamId) {

        String UserId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            teamService.cancelTeam(teamId, UserId);
            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "모임이 취소되었습니다.", null));
        } catch (Exception e) {
            log.error("모임 취소 처리 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "모임 취소 실패", e.getMessage()));
        }
    }

    // 참여 신청
    @PostMapping("/{teamId}/apply/{userId}")
    @Operation(summary = "모임 참여 신청", description = "사용자가 모임에 참여 신청을 합니다.")
    public ResponseEntity<ApiResponse> applyToJoinTeam(@PathVariable Long teamId) {

        String UserId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            User user = userService.getUserById(UserId);
            teamService.addParticipant(teamId, user);

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "참여 신청 성공", null));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "참여 신청 실패", e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "서버 오류 발생", e.getMessage()));
        }
    }


    // 승인 처리
    @PostMapping("/{teamId}/approve/{participantId}")
    @Operation(summary = "참여자 승인", description = "모임에 참여한 참가자를 승인합니다.")
    public ResponseEntity<ApiResponse> approveParticipant(@PathVariable Long teamId, @PathVariable Long participantId) {

        String UserId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            teamService.approveParticipant(participantId, UserId);
            Team team = teamService.getTeamById(teamId);
            TeamDto teamDto = convertToTeamDto(team);

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "참여자가 승인되었습니다.", teamDto));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "참여자 승인 실패", e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "서버 오류 발생", e.getMessage()));
        }
    }

    // 거절 처리
    @PostMapping("/{teamId}/reject/{participantId}")
    @Operation(summary = "참여자 거절", description = "모임에 참여한 참가자를 거절합니다.")
    public ResponseEntity<ApiResponse> rejectParticipant(@PathVariable Long teamId, @PathVariable Long participantId) {

        String UserId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            teamService.rejectParticipant(participantId, UserId);
            Team team = teamService.getTeamById(teamId);
            TeamDto teamDto = convertToTeamDto(team);
            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "참여자가 거절되었습니다.", teamDto));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "참여자 거절 실패", e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "서버 오류 발생", e.getMessage()));
        }
    }

    //주최자 모임 삭제 (주최자 -> Activated = false, 실제로는 비활성화)
    @DeleteMapping("/{teamId}/deactivate")
    @Operation(summary = "모임 비활성화", description = "모임을 비활성화합니다.")
    public ResponseEntity<ApiResponse> unActivatedTeamByLeader(@PathVariable Long teamId) {

        String UserId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            teamService.unActivatedTeamByLeader(teamId, UserId);
            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "모임이 삭제되었습니다.", null));
        } catch (Exception e) {
            log.error("모임 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "삭제 실패", e.getMessage()));
        }
    }

    private TeamDto convertToTeamDto(Team team) {
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamTitle(team.getTeamTitle());
        teamDto.setTeamDetails(team.getTeamDetails());
        teamDto.setTeamDate(team.getTeamDate());
        teamDto.setRestaurantName(team.getRestaurantName());
        teamDto.setMaxPeople(team.getMaxPeople());
        teamDto.setNowPeople(team.getNowPeople());
        teamDto.setImageUrl(team.getImageUrl());
        return teamDto;
    }


}

