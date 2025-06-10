package com.grepp.matnam.app.model.notification.repository;

import java.util.List;

public interface NotificationRepositoryCustom {

    long markAsReadByIds(String userId, List<Long> notificationIds);

    long deactivateById(String userId, Long notificationId);
}
