package com.grepp.matnam.app.model.sse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Repository
public class SseRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
        log.debug("Emitter 저장됨 - 사용자 ID: {}", userId);
    }

    public SseEmitter get(String userId) {
        return emitters.get(userId);
    }

    public void delete(String userId) {
        emitters.remove(userId);
        log.debug("Emitter 삭제됨 - 사용자 ID: {}", userId);
    }

    public void sendShutdownEventToAllEmitters() {
        log.info("모든 Emitter에 shutdown 이벤트 전송 시작");
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("shutdown").data("서버가 곧 재시작됩니다."));
                log.debug("shutdown 이벤트 전송 성공 - 사용자 ID: {}", userId);
            } catch (IOException e) {
                log.warn("shutdown 이벤트 전송 실패 - 사용자 ID: {}, 오류: {}", userId, e.getMessage());
            }
        });
    }

    public void clearAllEmittersImmediately() {
        log.info("강제 종료 - SSE emitter 즉시 complete 시작");
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.complete();
                log.debug("Emitter 완료 - 사용자 ID: {}", userId);
            } catch (Exception e) {
                log.warn("Emitter 종료 중 예외 발생 - 사용자 ID: {}, 오류: {}", userId, e.getMessage());
            }
        });

        // 🚨 강제 컨테이너 반영: 비동기 컨텍스트 종료 요청
        RequestContextHolder.resetRequestAttributes();

        emitters.clear();
        log.info("강제 종료 - 모든 emitter complete 및 정리 완료");
    }
}

