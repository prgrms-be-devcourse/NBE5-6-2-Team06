package com.grepp.matnam.app.controller.api.mymap;

import com.grepp.matnam.app.model.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/team/map")
@RequiredArgsConstructor
public class MymapTeamApiController {

    private final TeamService teamService;

    @GetMapping("/{teamId}")
    public ResponseEntity<List<Map<String, Object>>> getTeamMapData(@PathVariable Long teamId) {
        List<Map<String, Object>> result = teamService.getParticipantMymapData(teamId);
        return ResponseEntity.ok(result);
    }
}