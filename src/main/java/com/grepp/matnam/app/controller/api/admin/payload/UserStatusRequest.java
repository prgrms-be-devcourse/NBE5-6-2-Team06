package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.user.code.Status;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserStatusRequest {

    private Status status;

    private Integer suspendDuration;

    private String dueReason;

}
