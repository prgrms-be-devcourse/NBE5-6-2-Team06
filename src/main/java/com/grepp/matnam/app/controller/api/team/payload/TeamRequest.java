package com.grepp.matnam.app.controller.api.team.payload;

import com.grepp.matnam.app.model.team.entity.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequest {
    private Team team;
    private String userId;
}

