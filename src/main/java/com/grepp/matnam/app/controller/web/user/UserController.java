package com.grepp.matnam.app.controller.web.user;

import com.grepp.matnam.app.model.team.TeamReviewService;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.auth.AuthenticationUtils;
import com.grepp.matnam.infra.auth.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

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
        if (CookieUtils.getJwtToken(request).isEmpty() && !AuthenticationUtils.isAuthenticated()) {
            return "redirect:/user/signin";
        }

        return "user/preference";
    }

    @Transactional
    @GetMapping("/mypage")
    public String mypage(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size) {
        log.info("마이페이지 접근 시도");

        if (!AuthenticationUtils.isAuthenticated()) {
            log.warn("인증되지 않은 사용자의 마이페이지 접근 시도");
            return "redirect:/user/signin";
        }

        try {
            String userId = AuthenticationUtils.getCurrentUserId();
            log.info("인증된 사용자 ID: {}", userId);

            User user = userService.getUserById(userId);
            log.info("사용자 정보 조회 성공: {}", user.getUserId());

            // 통계 데이터
            model.addAttribute("stats", teamService.getUserStats(userId));

            // 리뷰 정보
            List<Team> completedTeams = teamService.getTeamsByParticipant(userId).stream()
                    .filter(team -> team.getStatus() == Status.COMPLETED)
                    .toList();

            List<Team> teamsWithoutReview = new ArrayList<>();
            for (Team team : completedTeams) {
                boolean hasCompletedAllReviews = teamReviewService.hasUserCompletedAllReviews(team.getTeamId(), userId);
                if (!hasCompletedAllReviews) {
                    teamsWithoutReview.add(team);
                }
            }

            int totalTeamsWithoutReview = teamsWithoutReview.size();
            int totalPages = (int) Math.ceil((double) totalTeamsWithoutReview / size);

            int start = page * size;
            int end = Math.min(start + size, totalTeamsWithoutReview);

            List<Team> paginatedTeamsWithoutReview =
                    start < totalTeamsWithoutReview ? teamsWithoutReview.subList(start, end) : new ArrayList<>();

            // 모델에 데이터 추가
            model.addAttribute("user", user);
            model.addAttribute("teamsWithoutReview", paginatedTeamsWithoutReview);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            // 주최한 모임 조회
            List<Team> hostingTeams = teamService.getTeamsByLeader(userId);

            // 참여 중인 모임 조회 (승인된 참여자 상태)
            List<Team> participatingTeams = teamService.getTeamsByParticipant(userId);
            participatingTeams.removeAll(hostingTeams);

            // 참여한 모든 모임 조회 (승인된 참여자 및 상태 무관)
            List<Team> allTeamsForUser = teamService.getAllTeamsForUser(userId);

            model.addAttribute("hostingTeams", hostingTeams);
            model.addAttribute("participatingTeams", participatingTeams);
            model.addAttribute("allTeamsForUser", allTeamsForUser);
            model.addAttribute("userMaps", new ArrayList<>());

            return "user/mypage";
        } catch (Exception e) {
            log.error("마이페이지 로딩 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/user/signin?error=profile_load_failed";
        }
    }

    @PostMapping("/deactivate")
    public String deactivateAccount(
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            String currentUserId = AuthenticationUtils.getCurrentUserId();
            userService.deactivateAccount(currentUserId, password);

            CookieUtils.clearAllCookies(request, response);

            AuthenticationUtils.clearSecurityContext();

            return "redirect:/?message=account_deactivated";
        } catch (Exception e) {
            return "redirect:/user/mypage?error=" + e.getMessage();
        }
    }

    @GetMapping("/password/change")
    public String passwordChange() {
        if (!AuthenticationUtils.isAuthenticated()) {
            return "redirect:/user/signin";
        }

        return "user/passwordChange";
    }
}