package com.grepp.matnam.app.controller.web.user;

import com.grepp.matnam.app.model.team.TeamReviewService;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TeamService teamService;
    private final TeamReviewService teamReviewService;

    @GetMapping("/signup")
    public String signupPage() {
        return "user/signup";
    }

    @GetMapping("/signin")
    public String signin() {
        return "user/signin";
    }

    @GetMapping("/preference")
    public String preference(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                boolean hasJwtToken = false;
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        hasJwtToken = true;
                        break;
                    }
                }
                if (!hasJwtToken) {
                    return "redirect:/user/signin";
                }
            } else {
                return "redirect:/user/signin";
            }
        }

        return "user/preference";
    }

    @Transactional
    @GetMapping("/mypage")
    public String mypage(Model model) {
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

            // 통계 데이터 (임시)
            model.addAttribute("stats", new HashMap<String, Integer>() {{
                put("hostingCount", 3);
                put("participatingCount", 5);
                put("restaurantCount", 2);
            }});

            // 리뷰 정보
            List<Team> teamsWithoutReview = new ArrayList<>();

            List<Team> completedTeams = teamService.getTeamsByParticipant(userId).stream()
                    .filter(team -> team.getStatus() == Status.COMPLETED)
                    .toList();

            for (Team team : completedTeams) {
                boolean hasCompletedAllReviews = teamReviewService.hasUserCompletedAllReviews(team.getTeamId(), userId);
                if (!hasCompletedAllReviews) {
                    teamsWithoutReview.add(team);
                }
            }

            model.addAttribute("user", user);
            model.addAttribute("teamsWithoutReview", teamsWithoutReview);

            // 주최한 모임 조회
            List<Team> hostingTeams = teamService.getTeamsByLeader(userId);
            model.addAttribute("hostingTeams", hostingTeams);

            // 참여 중인 모임 조회 (승인된 참여자 상태)
            List<Team> participatingTeams = teamService.getTeamsByParticipant(userId);
            model.addAttribute("participatingTeams", participatingTeams);

            // 참여한 모든 모임 조회 (승인된 참여자 및 상태 무관)
            List<Team> allTeamsForUser = teamService.getAllTeamsForUser(userId);
            model.addAttribute("allTeamsForUser", allTeamsForUser);

            // 빈 목록 추가 (실제 구현 전까지 사용)
//            model.addAttribute("allTeams", new ArrayList<>());
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