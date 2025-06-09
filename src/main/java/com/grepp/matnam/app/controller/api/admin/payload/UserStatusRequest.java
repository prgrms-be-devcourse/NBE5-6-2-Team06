package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.user.code.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserStatusRequest {

    @NotNull
    private Status status;

    @NotNull
    private Integer suspendDuration;

    @NotBlank
    private String dueReason;

}
