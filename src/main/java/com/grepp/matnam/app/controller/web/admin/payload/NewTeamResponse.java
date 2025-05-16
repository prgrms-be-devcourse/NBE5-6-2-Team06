package com.grepp.matnam.app.controller.web.admin.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewTeamResponse {
    private long newTeams;
    private String newTeamGrowth;
}
