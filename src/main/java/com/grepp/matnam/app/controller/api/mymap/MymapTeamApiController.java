package com.grepp.matnam.app.controller.api.mymap;

import com.grepp.matnam.app.model.team.TeamService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/team/map")
@RequiredArgsConstructor
@Tag(name = "Team Mymap API", description = "팀 참여자의 맛집 지도 관련 API")
public class MymapTeamApiController {

    private final TeamService teamService;

    @GetMapping("/{teamId}")
    @Operation(
            summary = "팀 참여자의 맛집 지도 데이터 조회",
            description = "특정 팀 ID에 속한 모든 참여자의 공개 상태인 맛집 지도 데이터를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 데이터가 반환됨"),
            @ApiResponse(responseCode = "404", description = "해당 팀을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<List<Map<String, Object>>> getTeamMapData(@PathVariable Long teamId) {
        List<Map<String, Object>> result = teamService.getParticipantMymapData(teamId);
        return ResponseEntity.ok(result);
    }
}
