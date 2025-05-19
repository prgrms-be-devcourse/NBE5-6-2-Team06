package com.grepp.matnam.infra.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        log.info("접근 권한 없는 요청: {}", request.getRequestURI());

        // Accept 헤더에 따라 HTML 또는 JSON 응답 분기 처리
        String acceptHeader = request.getHeader("Accept");

        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            // API 요청에 대한 JSON 응답
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"code\":\"4003\",\"message\":\"접근 권한 없음\",\"data\":\"해당 기능에 접근할 권한이 없습니다.\"}"
            );
        } else {
            // 메인 페이지로 직접 리다이렉트
            String targetUrl = "/";

            // 쿼리 파라미터로 토스트 메시지 전달
            targetUrl += "?toast_message=" + java.net.URLEncoder.encode("접근 권한이 없습니다.", "UTF-8") +
                    "&toast_type=error";

            response.sendRedirect(targetUrl);
        }
    }
}