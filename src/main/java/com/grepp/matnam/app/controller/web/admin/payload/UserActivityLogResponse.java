package com.grepp.matnam.app.controller.web.admin.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserActivityLogResponse {

    private long todayActiveUsers;
    private String activeUserGrowth;
}
