package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.team.entity.Team;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportTeamResponse {
    private String teamTitle;
    private String teamDetails;
    private String createdAt;

    public ReportTeamResponse(Team team) {
        this.teamTitle = team.getTeamTitle();
        this.teamDetails = team.getTeamDetails();
        this.createdAt = team.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }
}
