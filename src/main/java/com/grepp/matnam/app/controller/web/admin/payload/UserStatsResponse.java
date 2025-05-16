package com.grepp.matnam.app.controller.web.admin.payload;

import lombok.Data;

@Data
public class UserStatsResponse {
    private long totalUsers;
    private long activatedUsers;
    private long newUsers;
    private long stopUsers;
    private long inactivatedUsers;

    private long totalMaleUsers;
    private long activatedMaleUsers;
    private long newMaleUsers;
    private long stopMaleUsers;
    private long inactivatedMaleUsers;

    private long totalFemaleUsers;
    private long activatedFemaleUsers;
    private long newFemaleUsers;
    private long stopFemaleUsers;
    private long inactivatedFemaleUsers;
}
