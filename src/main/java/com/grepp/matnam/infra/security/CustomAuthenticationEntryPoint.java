package com.grepp.matnam.infra.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        log.info("인증되지 않은 접근 시도: {}", request.getRequestURI());

        // Accept 헤더에 따라 HTML 또는 JSON 응답 분기 처리
        String acceptHeader = request.getHeader("Accept");

        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            // API 요청에 대한 JSON 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"code\":\"4001\",\"message\":\"인증 실패\",\"data\":\"로그인 후 이용해주세요.\"}"
            );
        } else {
            // 로그인 페이지로 직접 리다이렉트
            String targetUrl = "/user/signin";

            // 쿼리 파라미터로 토스트 메시지 전달
            targetUrl += "?toast_message=" + java.net.URLEncoder.encode("로그인 후 이용해주세요.", "UTF-8") +
                    "&toast_type=warning";

            response.sendRedirect(targetUrl);
        }
    }
}