package com.grepp.matnam.app.controller.web.admin.payload;

import lombok.Data;

@Data
public class TeamStatsResponse {
    private long totalTeams;
    private long activeTeams;
    private long completedTeams;
    private long newTeamsLast30Days;
    private double averageTeamSize;
}
