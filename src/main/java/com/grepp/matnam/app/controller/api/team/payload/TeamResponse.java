package com.grepp.matnam.app.controller.api.team.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamResponse {
    private Long teamId;
    private String teamName;
    private String userId;
    private String userNickname;
}
