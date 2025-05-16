package com.grepp.matnam.infra.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuthenticationUtils {

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            log.warn("인증되지 않은 사용자의 접근 시도");
            throw new IllegalStateException("사용자 인증이 필요합니다.");
        }

        String userId = authentication.getName();
        log.debug("현재 인증된 사용자: {}", userId);
        return userId;
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !"anonymousUser".equals(authentication.getName());
    }

    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
        log.debug("보안 컨텍스트 초기화 완료");
    }
}