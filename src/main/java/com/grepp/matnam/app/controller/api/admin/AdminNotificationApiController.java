package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.notification.payload.BroadcastNotificationRequest;
import com.grepp.matnam.app.model.notification.service.NotificationService;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.infra.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notification")
@Slf4j
@Tag(name = "Admin Notification API", description = "관리자 - 알림 관리 API")
public class AdminNotificationApiController {

    private final UserService userService;
    private final NotificationService notificationService;

    @PostMapping("/broadcast")
    @Operation(summary = "공지사항 발송", description = "모든 활성 사용자들에게 공지사항을 발송합니다.")
    public ResponseEntity<ApiResponse<Void>> broadcastNotice(
        @RequestBody BroadcastNotificationRequest request) {
        userService.sendBroadcastNotification(request.getContent());
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @DeleteMapping("/{noticeId}")
    @Operation(summary = "공지사항 비활성화", description = "특정 공지사항을 비활성화합니다.")
    public ResponseEntity<ApiResponse<Void>> unActivatedNotice(@PathVariable Long noticeId) {
        notificationService.unActivatedById(noticeId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @PostMapping("/{noticeId}")
    @Operation(summary = "공지사항 재발송", description = "기존 공지사항을 모든 활성 사용자에게 재발송합니다.")
    public ResponseEntity<ApiResponse<Void>> resendNotice(@PathVariable Long noticeId) {
        userService.resendBroadcastNotification(noticeId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
