package com.grepp.matnam.app.controller.api.sse;

import com.grepp.matnam.app.model.sse.SseService;
import com.grepp.matnam.infra.auth.AuthenticationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "SSE API", description = "실시간 알림 구독 API")
public class SseApiController implements SmartLifecycle {

    private final SseService sseService;
    private volatile boolean isRunning = false;

    @GetMapping(value = "/api/sse/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "SSE 구독", description = "실시간 알림을 받기 위한 SSE 구독을 진행합니다.")
    public SseEmitter subscribe() {
        String userId = AuthenticationUtils.getCurrentUserId();
        SseEmitter emitter = sseService.subscribe(userId);

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