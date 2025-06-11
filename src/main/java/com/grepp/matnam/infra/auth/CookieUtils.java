package com.grepp.matnam.infra.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class CookieUtils {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setHttpOnly(false); // XSS 공격 방지
        cookie.setSecure(false);

        response.addCookie(cookie);
        log.debug("쿠키 생성: {}, 유효시간: {}", name, maxAge);
    }

    public static void addJwtCookie(HttpServletResponse response, String token, int maxAge) {
        addCookie(response, "jwtToken", token, maxAge, "/");
    }

    public static void addUserIdCookie(HttpServletResponse response, String userId, int maxAge) {
        addCookie(response, "userId", userId, maxAge, "/");
    }

    public static void addUserRoleCookie(HttpServletResponse response, String role, int maxAge) {
        addCookie(response, "userRole", role, maxAge, "/");
    }

    public static void addUserNicknameCookie(HttpServletResponse response, String nickname, int maxAge) {
        String encodedNickname = URLEncoder.encode(nickname, StandardCharsets.UTF_8);
        addCookie(response, "userNickname", encodedNickname, maxAge, "/");
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> name.equals(cookie.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }

    public static Optional<String> getJwtToken(HttpServletRequest request) {
        return getCookie(request, "jwtToken")
                .map(Cookie::getValue);
    }

    public static Optional<String> getUserNicknameCookie(HttpServletRequest request) {
        return getCookie(request, "userNickname")
                .map(Cookie::getValue);
    }

    public static void clearAllCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        log.debug("모든 쿠키 삭제 완료");
    }
}