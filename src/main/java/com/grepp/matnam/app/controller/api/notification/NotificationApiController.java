package com.grepp.matnam.app.controller.api.notification;

import com.grepp.matnam.app.controller.api.notification.payload.NotificationIdsRequest;
import com.grepp.matnam.app.model.notification.entity.Notification;
import com.grepp.matnam.app.model.notification.service.NotificationService;
import com.grepp.matnam.infra.auth.AuthenticationUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/notification")
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount(currentUserId));
    }

    @GetMapping("")
    public ResponseEntity<List<Notification>> getAll() {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getAllNotifications(currentUserId));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread() {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getUnreadNotifications(currentUserId));
    }

    @GetMapping("/system")
    public ResponseEntity<List<Notification>> getSystem() {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getSystemNotifications(currentUserId));
    }

    @PostMapping("/mark-read")
    public ResponseEntity<Void> markAsRead(@RequestBody NotificationIdsRequest request) {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        notificationService.markAsRead(currentUserId, request.getNotificationIds());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        notificationService.deactivateNotification(currentUserId, notificationId);
        return ResponseEntity.ok().build();
    }

}
