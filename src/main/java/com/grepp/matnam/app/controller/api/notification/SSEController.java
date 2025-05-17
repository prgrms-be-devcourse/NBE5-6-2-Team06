package com.grepp.matnam.app.controller.api.notification;

import com.grepp.matnam.app.model.notification.service.NotificationService;
import com.grepp.matnam.infra.auth.AuthenticationUtils;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SSEController {

    private final NotificationService notificationService;
    private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/api/sse/notification/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        String userId = AuthenticationUtils.getCurrentUserId();
        SseEmitter emitter = new SseEmitter(3600000L);

        emitters.put(userId, emitter);
        log.info("SSE 연결 수립 - 사용자 ID: {}", userId);

        emitter.onCompletion(() -> {
            emitters.remove(userId);
            log.info("SSE 연결 완료 - 사용자 ID: {}", userId);
        });
        emitter.onTimeout(() -> {
            emitters.remove(userId);
            log.info("SSE 연결 타임아웃 - 사용자 ID: {}", userId);
        });
        emitter.onError((throwable) -> {
            emitters.remove(userId);
            log.error("SSE 연결 오류 - 사용자 ID: {}, 오류: {}", userId, throwable.getMessage());
        });

        // 최초 연결 시 읽지 않은 알림 개수 전송
        try {
            long unreadCount = notificationService.getUnreadNotificationCount(userId);
            emitter.send(SseEmitter.event()
                .name("unreadCount")
                .data(unreadCount));
            log.debug("최초 연결 시 읽지 않은 알림 개수 전송 - 사용자 ID: {}, 개수: {}", userId, unreadCount);
        } catch (IOException e) {
            log.error("최초 연결 시 읽지 않은 알림 개수 전송 실패 - 사용자 ID: {}, 오류: {}", userId, e.getMessage());
        }

        return emitter;
    }

    public void sendNotificationToUser(String userId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
                log.debug("SSE 이벤트 전송 - 사용자 ID: {}, 이벤트: {}, 데이터: {}", userId, eventName, data);
            } catch (IOException e) {
                emitters.remove(userId);
                log.error("SSE 이벤트 전송 실패 (연결 끊김) - 사용자 ID: {}, 이벤트: {}, 오류: {}", userId, eventName,
                    e.getMessage());
            }
        } else {
            log.warn("SSE 연결 없음 - 사용자 ID: {}", userId);
        }
    }
}
