package com.grepp.matnam.app.controller.api.notification;

import com.grepp.matnam.app.controller.api.notification.payload.NotificationIdsRequest;
import com.grepp.matnam.app.model.notification.entity.Notification;
import com.grepp.matnam.app.model.notification.service.NotificationService;
import com.grepp.matnam.infra.auth.AuthenticationUtils;
import com.grepp.matnam.infra.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Notification API", description = "알림 관리 API")
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/unread-count")
    @Operation(summary = "읽지 않은 알림 개수 조회", description = "읽지 않은 알림의 개수를 조회합니다.")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(notificationService.getUnreadNotificationCount(currentUserId)));
    }

    @GetMapping("")
    @Operation(summary = "모든 알림 조회", description = "모든 알림을 조회합니다.")
    public ResponseEntity<ApiResponse<List<Notification>>> getAll() {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(notificationService.getAllNotifications(currentUserId)));
    }

    @GetMapping("/unread")
    @Operation(summary = "읽지 않은 알림 조회", description = "읽지 않은 알림을 조회합니다.")
    public ResponseEntity<ApiResponse<List<Notification>>> getUnread() {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(notificationService.getUnreadNotifications(currentUserId)));
    }

    @GetMapping("/system")
    @Operation(summary = "시스템 알림 조회", description = "시스템(공지사항) 알림을 조회합니다.")
    public ResponseEntity<ApiResponse<List<Notification>>> getSystem() {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(notificationService.getSystemNotifications(currentUserId)));
    }

    @PostMapping("/mark-read")
    @Operation(summary = "알림 읽음 처리", description = "알림을 읽음 처리합니다.")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@RequestBody @Valid NotificationIdsRequest request) {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        notificationService.markAsRead(currentUserId, request.getNotificationIds());
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림 비활성화", description = "특정 알림을 비활성화합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long notificationId) {
        String currentUserId = AuthenticationUtils.getCurrentUserId();
        notificationService.deactivateNotification(currentUserId, notificationId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

}
