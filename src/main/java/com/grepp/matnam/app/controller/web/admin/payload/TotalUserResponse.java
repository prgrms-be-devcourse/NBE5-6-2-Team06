package com.grepp.matnam.app.controller.web.admin.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalUserResponse {

    private long totalUsers;
    private String totalUserGrowth;
}
