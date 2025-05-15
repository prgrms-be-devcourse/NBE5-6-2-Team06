package com.grepp.matnam.app.controller.api.team;

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
    @Operation(summary = "모임 참여 신청", description = "사용자가 모임에 참여 신청을 합니다.")
    public ResponseEntity<ApiResponse> applyToJoinTeam(@PathVariable Long teamId, @PathVariable String userId) {
        try {
            // 유저 정보 가져오기
            User user = userService.getUserById(userId);

            // 참여자 추가
            teamService.addParticipant(teamId, user);

            // 성공 시 응답 반환
            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "참여 신청 성공", null));

        } catch (RuntimeException e) {
            // 예외 발생 시 오류 처리
            return ResponseEntity.badRequest()
                .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "참여 신청 실패", e.getMessage()));

        } catch (Exception e) {
            // 일반적인 서버 오류 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "서버 오류 발생", e.getMessage()));
        }
    }


    // 승인 처리
    @PostMapping("/{teamId}/approve/{participantId}")
    @Operation(summary = "참여자 승인", description = "모임에 참여한 참가자를 승인합니다.")
    public ResponseEntity<ApiResponse> approveParticipant(@PathVariable Long teamId, @PathVariable Long participantId) {
        try {
            // 승인 처리
            teamService.approveParticipant(participantId);

            // 팀 정보 조회 (DTO로 변환)
            Team team = teamService.getTeamById(teamId);
            TeamDto teamDto = convertToTeamDto(team); // 이 메서드를 추가해야 합니다.

            // 성공 응답 반환 (DTO 포함)
            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "참여자가 승인되었습니다.", teamDto));

        } catch (RuntimeException e) {
            // 예외 발생 시 오류 처리
            return ResponseEntity.badRequest()
                .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "참여자 승인 실패", e.getMessage()));

        } catch (Exception e) {
            // 일반적인 서버 오류 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "서버 오류 발생", e.getMessage()));
        }
    }

    private TeamDto convertToTeamDto(Team team) {
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamTitle(team.getTeamTitle());
        teamDto.setTeamDetails(team.getTeamDetails());
        teamDto.setMeetDate(team.getMeetDate());
        teamDto.setRestaurantName(team.getRestaurantName());
        teamDto.setMaxPeople(team.getMaxPeople());
        teamDto.setNowPeople(team.getNowPeople());
        teamDto.setImageUrl(team.getImageUrl());
        return teamDto;
    }


}

