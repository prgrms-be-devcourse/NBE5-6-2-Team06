package com.grepp.matnam.app.controller.api.notification.payload;

import java.util.List;
import lombok.Data;

@Data
public class NotificationIdsRequest {
    private List<Long> notificationIds;
}
