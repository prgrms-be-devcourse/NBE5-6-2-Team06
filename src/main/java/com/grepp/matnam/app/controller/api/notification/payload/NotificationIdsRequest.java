package com.grepp.matnam.app.controller.api.notification.payload;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class NotificationIdsRequest {
    @NotNull
    private List<Long> notificationIds;
}
