package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.team.code.Status;
import lombok.Data;

@Data
public class TeamStatusUpdateRequest {
    private Status status;
    private String reason;
}
