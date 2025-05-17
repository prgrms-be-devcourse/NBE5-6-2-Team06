package com.grepp.matnam.app.model.sse;

import com.grepp.matnam.app.model.notification.service.NotificationService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    private final NotificationService notificationService;
    private final SseRepository sseRepository;

    private static final Long DEFAULT_TIMEOUT = 60 * 60 * 1000L; // 1시간

    /**
     * SSE 구독
     */
    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        sseRepository.save(userId, emitter);

        log.info("SSE 연결 수립 - 사용자 ID: {}", userId);

        // 연결 종료 처리
        emitter.onCompletion(() -> {
            sseRepository.delete(userId);
            log.info("SSE 연결 완료 - 사용자 ID: {}", userId);
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            sseRepository.delete(userId);
            log.info("SSE 연결 타임아웃 - 사용자 ID: {}", userId);
        });

        emitter.onError((throwable) -> {
            emitter.completeWithError(throwable);
            sseRepository.delete(userId);
            log.error("SSE 연결 오류 - 사용자 ID: {}, 오류: {}", userId, throwable.getMessage());
        });

        // 최초 연결 시 읽지 않은 알림 개수 전송
        try {
            long unreadCount = notificationService.getUnreadNotificationCount(userId);
            emitter.send(SseEmitter.event()
                .name("unreadCount")
                .data(unreadCount, MediaType.APPLICATION_JSON));
            log.debug("최초 연결 시 unreadCount 전송 - 사용자 ID: {}, 개수: {}", userId, unreadCount);
        } catch (IOException e) {
            log.error("초기 unreadCount 전송 실패 - 사용자 ID: {}, 오류: {}", userId, e.getMessage());
        }

        return emitter;
    }

    /**
     * 특정 사용자에게 SSE 이벤트 전송
     */
    public void sendNotificationToUser(String userId, String eventName, Object data) {
        SseEmitter emitter = sseRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
                log.debug("SSE 이벤트 전송 - 사용자 ID: {}, 이벤트: {}, 데이터: {}", userId, eventName, data);
            } catch (IOException e) {
                sseRepository.delete(userId);
                log.error("SSE 이벤트 전송 실패 - 사용자 ID: {}, 오류: {}", userId, e.getMessage());
            }
        } else {
            log.debug("SSE 연결 없음 - 사용자 ID: {}", userId);
        }
    }

}
