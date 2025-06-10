package com.grepp.matnam.app.controller.api.team;

import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.dto.TeamDto;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Team API", description = "모임 관련 REST API")
public class TeamApiController {

    private final TeamService teamService;
    private final UserService userService;


    // 모임 상태 변경 - 모임 완료
    @PutMapping("/{teamId}/complete")
    @Operation(summary = "모임 완료", description = "주최자가 모임 완료 처리 합니다")
    public ResponseEntity<ApiResponse<String>> completeTeam(@PathVariable Long teamId,
        @RequestParam Status status) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            long approvedCount = teamService.getParticipantCountExcludingHost(teamId);
            if (approvedCount < 1) {
                return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error(
                        ResponseCode.BAD_REQUEST,
                        "참가자가 없어 모임을 완료할 수 없습니다."
                    ));
            }

            teamService.completeTeam(teamId, status, userId);
            return ResponseEntity.ok(ApiResponse.success("모임이 성공적으로 완료 처리되었습니다."));
        } catch (Exception e) {
            log.error("모임 완료 처리 실패: {}", e.getMessage());
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                    ResponseCode.BAD_REQUEST,
                    e.getMessage()
                ));
        }
    }

    // 모임 상태 변경 - 모임 취소
    @PostMapping("/{teamId}/cancel")
    @Operation(summary = "모임 취소", description = "주최자가 모임 취소 처리 합니다")
    public ResponseEntity<ApiResponse<String>> cancelTeam(@PathVariable Long teamId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            teamService.cancelTeam(teamId, userId);
            return ResponseEntity.ok(
                ApiResponse.success("모임이 정상적으로 취소되었습니다.")
            );
        } catch (Exception e) {
            log.error("모임 취소 처리 실패: {}", e.getMessage());
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                    ResponseCode.BAD_REQUEST,
                    e.getMessage()
                ));
        }
    }

    // 참여 신청
    @PostMapping("/{teamId}/apply")
    @Operation(summary = "모임 참여 신청", description = "사용자가 모임에 참여 신청을 합니다.")
    public ResponseEntity<ApiResponse<String>> applyToJoinTeam(@PathVariable Long teamId) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            User user = userService.getUserById(userId);
            teamService.addParticipant(teamId, user);

            return ResponseEntity.ok(ApiResponse.success("모임 신청에 성공했습니다."));

        } catch (RuntimeException e) {
            log.error("모임 참여 신청 실패 : {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(ResponseCode.BAD_REQUEST, e.getMessage()));

        } catch (Exception e) {
            log.error("모임 참여 신청 실패 : {}", e.getMessage() ,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }


    // 승인 처리
    @PostMapping("/{teamId}/approve/{participantId}")
    @Operation(summary = "참여자 승인", description = "모임에 참여한 참가자를 승인합니다.")
    public ResponseEntity<ApiResponse<Object>> approveParticipant(@PathVariable Long teamId,
        @PathVariable Long participantId) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            teamService.approveParticipant(participantId, userId);
            Team team = teamService.getTeamById(teamId);
            TeamDto teamDto = convertToTeamDto(team);

            return ResponseEntity.ok(ApiResponse.success(teamDto));

        } catch (RuntimeException e) {
            log.error("참여자 승인 실패 : {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(ResponseCode.BAD_REQUEST, e.getMessage()));

        } catch (Exception e) {
            log.error("참여자 승인 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    // 거절 처리
    @PostMapping("/{teamId}/reject/{participantId}")
    @Operation(summary = "참여자 거절", description = "모임에 참여한 참가자를 거절합니다.")
    public ResponseEntity<ApiResponse<Object>> rejectParticipant(@PathVariable Long teamId,
        @PathVariable Long participantId) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            teamService.rejectParticipant(participantId, userId);
            Team team = teamService.getTeamById(teamId);
            TeamDto teamDto = convertToTeamDto(team);
            return ResponseEntity.ok(ApiResponse.success(teamDto));

        } catch (RuntimeException e) {
            log.error("참여자 거절 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(ResponseCode.BAD_REQUEST, e.getMessage()));

        } catch (Exception e) {
            log.error("참여자 거절 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    //주최자 모임 삭제 (주최자 -> Activated = false, 실제로는 비활성화)
    @DeleteMapping("/{teamId}/deactivate")
    @Operation(summary = "모임 비활성화", description = "모임을 비활성화합니다.")
    public ResponseEntity<ApiResponse<String>> unActivatedTeamByLeader(@PathVariable Long teamId) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            teamService.unActivatedTeamByLeader(teamId, userId);
            return ResponseEntity.ok(ApiResponse.success("모임이 성공적으로 비활성화되었습니다."));
        } catch (RuntimeException e) {
            log.error("모임 비활성화 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(ResponseCode.BAD_REQUEST, e.getMessage()));
        } catch (Exception e) {
            log.error("모임 비활성화 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage()));
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
        teamDto.setCategory(team.getCategory());
        teamDto.setImageUrl(team.getImageUrl());
        return teamDto;
    }


}

