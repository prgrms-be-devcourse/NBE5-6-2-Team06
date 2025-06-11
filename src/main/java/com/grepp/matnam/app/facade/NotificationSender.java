package com.grepp.matnam.app.facade;

import com.grepp.matnam.app.model.notification.code.NotificationType;
import com.grepp.matnam.app.model.notification.entity.Notice;
import com.grepp.matnam.app.model.notification.entity.Notification;
import com.grepp.matnam.app.model.notification.repository.NotificationRepository;
import com.grepp.matnam.app.model.notification.service.NotificationService;
import com.grepp.matnam.app.model.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSender {
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final SseService sseService;

    /**
     * 알림 생성 + SSE 전송 + unreadCount 전송
     */
    public void sendNotificationToUser(String userId, NotificationType notificationType, String message, String link) {
        // 알림 저장
        Notification notification = notificationService.createNotification(userId, notificationType, message, link);

        // 알림 SSE 전송
        sseService.sendNotificationToUser(userId, "newMessage", notification);

        // 읽지 않은 알림 개수 전송
        long unreadCount = notificationRepository.countByUserIdAndIsReadFalseAndActivatedTrue(userId);
        sseService.sendNotificationToUser(userId, "unreadCount", unreadCount);
    }

    public void resendNotificationToUser(String userId, Notice notice) {
        Notification notification = notificationService.createNotification(userId, notice.getType(), notice.getMessage(), notice.getLink());

        // 알림 SSE 전송
        sseService.sendNotificationToUser(userId, "newMessage", notification);

        // 읽지 않은 알림 개수 전송
        long unreadCount = notificationRepository.countByUserIdAndIsReadFalseAndActivatedTrue(userId);
        sseService.sendNotificationToUser(userId, "unreadCount", unreadCount);
    }
}
