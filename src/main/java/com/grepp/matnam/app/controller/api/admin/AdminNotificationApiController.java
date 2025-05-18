package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.notification.payload.BroadcastNotificationRequest;
import com.grepp.matnam.app.model.notification.service.NotificationService;
import com.grepp.matnam.app.model.user.UserService;
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
public class AdminNotificationApiController {

    private final UserService userService;
    private final NotificationService notificationService;

    @PostMapping("/broadcast")
    public ResponseEntity<?> broadcastNotice(
        @RequestBody BroadcastNotificationRequest request) {
        userService.sendBroadcastNotification(request.getContent());
        return ResponseEntity.ok("공지사항이 성공적으로 발송되었습니다.");
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<?> unActivatedNotice(@PathVariable Long noticeId) {
        notificationService.unActivatedById(noticeId);
        return ResponseEntity.ok("공지사항이 비활성화되었습니다.");
    }

    @PostMapping("/{noticeId}")
    public ResponseEntity<?> resendNotice(@PathVariable Long noticeId) {
        userService.resendBroadcastNotification(noticeId);
        return ResponseEntity.ok("공지사항이 성공적으로 발송되었습니다.");
    }
}
