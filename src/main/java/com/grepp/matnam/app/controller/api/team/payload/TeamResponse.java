package com.grepp.matnam.app.controller.api.team.payload;

import com.grepp.matnam.app.model.team.entity.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamResponse {
    private Long teamId;
    private String teamName;
    private String userId;
    private String userNickname;

    public TeamResponse(Team team) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamTitle();
        this.userId = team.getUser().getUserId();
        this.userNickname = team.getUser().getNickname();
    }
}
