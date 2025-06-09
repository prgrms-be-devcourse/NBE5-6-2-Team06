package com.grepp.matnam.app.controller.api.notification.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BroadcastNotificationRequest {
    @NotBlank
    private String content;
}
