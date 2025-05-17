package com.grepp.matnam.app.model.sse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
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

    public Map<String, SseEmitter> findAll() {
        return emitters;
    }

    public boolean exists(String userId) {
        return emitters.containsKey(userId);
    }

    public void clear() {
        log.info("모든 Emitter 삭제");
        emitters.clear();
    }

}
