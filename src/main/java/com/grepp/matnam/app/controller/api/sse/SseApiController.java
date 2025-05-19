package com.grepp.matnam.app.controller.api.sse;

import com.grepp.matnam.app.model.sse.SseService;
import com.grepp.matnam.infra.auth.AuthenticationUtils;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SseApiController implements SmartLifecycle {

    private final SseService sseService;
    private volatile boolean isRunning = false;

    @GetMapping(value = "/api/sse/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(HttpServletResponse response) {
        String userId = AuthenticationUtils.getCurrentUserId();

        // SSE 헤더 수동 설정
        response.setHeader("Content-Type", "text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("X-Accel-Buffering", "no");

        SseEmitter emitter = sseService.subscribe(userId);

        try {
            response.flushBuffer();
        } catch (IOException e) {
            log.warn("Flush 실패 - userId: {}", userId, e);
        }

        return emitter;
    }

    @Override
    public void start() {
        log.info("SseController started");
        isRunning = true;
    }

    @Override
    public void stop() {
        log.info("SseController stopped - SSE connections 정리 시작");
        isRunning = false;
        sseService.clearAll();
        log.info("SseController stopped - SSE connections 정리 완료");
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }
}