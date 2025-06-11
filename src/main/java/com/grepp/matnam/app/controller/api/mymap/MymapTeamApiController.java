package com.grepp.matnam.app.controller.api.mymap;

import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            description = "특정 팀 ID에 속한 승인된 참여자들의 공개 상태 맛집 데이터를 조회합니다."
    )
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTeamMapData(@PathVariable Long teamId) {
        List<Map<String, Object>> result = teamService.getParticipantMymapData(teamId);
        return ResponseEntity
                .status(ResponseCode.OK.status())
                .body(ApiResponse.success(result));
    }
}
