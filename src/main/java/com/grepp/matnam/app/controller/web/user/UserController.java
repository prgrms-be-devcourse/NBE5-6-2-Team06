package com.grepp.matnam.app.controller.web.user;

import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage() {
        return "user/signup";
    }

    @GetMapping("/signin")
    public String signin() {
        return "user/signin";
    }

    @GetMapping("/preference")
    public String preference() {
        return "user/preference";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        boolean jwtCookieFound = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtCookieFound = true;
                    break;
                }
            }
        }
        log.info("마이페이지 접근 - JWT 쿠키 존재: " + jwtCookieFound);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "인증 정보 없음";
        log.info("마이페이지 접근 - 현재 인증 사용자: {}", currentUser);

        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            log.warn("인증되지 않은 사용자의 마이페이지 접근 시도");
            return "redirect:/user/signin";
        }

        try {
            String userId = authentication.getName();
            log.info("인증된 사용자 ID: {}", userId);

            User user = userService.getUserById(userId);
            log.info("사용자 정보 조회 성공: {}", user.getUserId());

            model.addAttribute("user", user);

            // 임시 통계 데이터 (실제 구현 전까지 사용)
            model.addAttribute("stats", new HashMap<String, Integer>() {{
                put("hostingCount", 3);
                put("participatingCount", 5);
                put("restaurantCount", 2);
            }});

            // 빈 목록 추가 (실제 구현 전까지 사용)
            model.addAttribute("allTeams", new ArrayList<>());
            model.addAttribute("userMaps", new ArrayList<>());

            return "user/mypage";
        } catch (Exception e) {
            log.error("마이페이지 로딩 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/user/signin?error=profile_load_failed";
        }
    }

    @PostMapping("/deactivate")
    public String deactivateAccount(@RequestParam("password") String password,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        try {
            String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.deactivateAccount(currentUserId, password);

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }

            SecurityContextHolder.clearContext();

            return "redirect:/?message=account_deactivated";
        } catch (Exception e) {
            return "redirect:/user/mypage?error=" + e.getMessage();
        }
    }

    @GetMapping("/password/change")
    public String passwordChange() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/user/signin";
        }

        return "user/passwordChange";
    }
}