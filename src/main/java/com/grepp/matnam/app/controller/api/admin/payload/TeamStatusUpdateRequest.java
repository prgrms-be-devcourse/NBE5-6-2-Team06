package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.team.code.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamStatusUpdateRequest {
    @NotNull
    private Status status;
    private String reason;
}
