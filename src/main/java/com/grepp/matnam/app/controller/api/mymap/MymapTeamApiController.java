package com.grepp.matnam.app.controller.api.mymap;

import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/team/map")
@RequiredArgsConstructor
public class MymapTeamApiController {

    private final TeamService teamService;

    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTeamMapData(@PathVariable Long teamId) {
        List<Map<String, Object>> result = teamService.getParticipantMymapData(teamId);
        return ResponseEntity
                .status(ResponseCode.OK.status())
                .body(ApiResponse.success(result));
    }
}