package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.controller.api.team.payload.TeamRequest;
import com.grepp.matnam.app.model.team.ParticipantRepository;
import com.grepp.matnam.app.model.team.TeamReviewRepository;
import com.grepp.matnam.app.model.team.TeamReviewService;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.team.entity.TeamReview;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/team")
@RequiredArgsConstructor
@Slf4j
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;
    private final TeamReviewService teamReviewService;
    private final TeamReviewRepository teamReviewRepository;
    private final ParticipantRepository participantRepository;


    // 모임 생성 페이지
    @GetMapping("/create")
    public String create() {
        return "team/teamCreate";
    }

    // 모임 생성
    @PostMapping("/create")
    public String createTeam(@ModelAttribute TeamRequest teamRequest, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // 현재 로그인한 사용자 ID 가져오기
        User user = userService.getUserById(userId);
        Team team = teamRequest.toDto(user);
        team.setUser(user);

        teamService.saveTeam(team);
        teamService.addParticipant(team.getTeamId(), user);

        return "redirect:/team/detail/" + team.getTeamId();
    }

    // 모임 검색 페이지
    @GetMapping("/search")
    public String searchTeams(
        @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
        Model model) {
        Page<Team> teamPage = teamService.getAllTeams(pageable);
        model.addAttribute("teams", teamPage.getContent());
        model.addAttribute("page", teamPage);
        return "team/teamSearch";
    }


    // 모임 상세 조회
    @GetMapping("/detail/{teamId}")
    public String teamDetail(@PathVariable Long teamId, Model model) {
        // 현재 로그인한 사용자 정보 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        Team team = teamService.getTeamByIdWithParticipants(teamId);
        model.addAttribute("team", team);

        boolean isParticipant = participantRepository.existsByUser_UserIdAndTeam_TeamId(userId, teamId);

        model.addAttribute("team", team);
        model.addAttribute("isParticipant", isParticipant);
        model.addAttribute("user", user);


        return "team/teamDetail";
    }


    // 팀 페이지 조회
    @GetMapping("/page/{teamId}")
    public String getTeamPage(@PathVariable Long teamId, Model model) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.getUserById(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("userNickname", currentUser.getNickname());
        model.addAttribute("teamId", teamId);

        Team team = teamService.getTeamByIdWithParticipants(teamId);
        model.addAttribute("team", team);
        if (team != null && team.getUser() != null) {
            model.addAttribute("leader", team.getUser());
        }

        List<Participant> approvedParticipants = team.getParticipants().stream()
            .filter(participant -> participant.getParticipantStatus() == ParticipantStatus.APPROVED)
            .toList();
        model.addAttribute("participants", approvedParticipants);

        return "team/teamPage";
    }

//    // 팀 페이지 조회 용
//    @GetMapping("/teamPage/{teamId}")
//    public String teamPage(@PathVariable Long teamId, Model model) {
//        Team team = teamService.getTeamByIdWithParticipants(teamId);
//        model.addAttribute("team", team);
//        return "team/teamPage";
//    }

    // 모임 참여 신청
    @PostMapping("/{teamId}/apply")
    public String applyToJoinTeam(@PathVariable Long teamId, @RequestParam String userId) {
        User user = userService.getUserById(userId);

        // 이미 신청한 참여자인지 확인
//        if (participantRepository.existsByUser_UserIdAndTeam_TeamId(userId, teamId)) {
//            return "redirect:/team/" + teamId + "/page?error=이미 신청한 모임입니다.";
//        }

        teamService.addParticipant(teamId, user);
//        return "redirect:/team/" + teamId + "/page";
        return "redirect:/team/detail/" + teamId;
    }

    // 모임 참여 수락 (주최자가 호출)
    @PostMapping("/participants/{teamId}/{userId}")
    public ResponseEntity<String> acceptParticipant(@PathVariable Long teamId,
        @PathVariable String userId) {
        try {
            teamService.acceptParticipant(teamId, userId);
            return ResponseEntity.ok("참가자가 수락되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("참가자 수락에 실패했습니다.");
        }
    }

    // 참여자 조회(팀 페이지)
//    @GetMapping("/{teamId}/participants")
//    public String getParticipants(@PathVariable Long teamId, Model model) {
//        List<Participant> participants = teamService.getParticipant(teamId);
//        model.addAttribute("teamId", teamId);
//        model.addAttribute("participants", participants);
//        return "team/teamPage";
//    }

//    // 사용자 전체 모임 조회
//    @GetMapping("/mypage")
//    public String mypageTeam(Model model) {
//
//        // 현재 로그인된 사용자 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//        User user = userService.getUserById(userId);
//
//        // 주최한 모임 조회
//        List<Team> hostingTeams = teamService.getTeamsByLeader(userId);
//        model.addAttribute("hostingTeams", hostingTeams);
//
//        // 참여 중인 모임 조회 (승인된 참여자 상태)
//        List<Team> participatingTeams = teamService.getTeamsByParticipant(userId);
//        model.addAttribute("participatingTeams", participatingTeams);
//
//        // 참여한 모든 모임 조회 (승인된 참여자 및 상태 무관)
//        List<Team> allTeamsForUser = teamService.getAllTeamsForUser(userId);
//        model.addAttribute("allTeamsForUser", allTeamsForUser);
//
//        model.addAttribute("user", user);
//        model.addAttribute("userMaps", new ArrayList<>());
//
//        return "user/mypage";
//    }

//    @GetMapping("/mypage")
//    public String mypageTeam(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUser = authentication != null ? authentication.getName() : "인증 정보 없음";
//        log.info("TeamController - 마이페이지 (모임 정보) 접근 - 현재 인증 사용자: {}", currentUser);
//
//        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
//            log.warn("TeamController - 인증되지 않은 사용자의 마이페이지 접근 시도");
//            return "redirect:/user/signin";
//        }
//
//        try {
//            String userId = authentication.getName();
//            log.info("TeamController - 인증된 사용자 ID: {}", userId);
//
//            User user = userService.getUserById(userId);
//            log.info("TeamController - 사용자 정보 조회 성공: {}", user.getUserId());
//            model.addAttribute("user", user);
//
//            // 주최한 모임 조회
//            List<Team> hostingTeams = teamService.getTeamsByLeader(userId);
//            model.addAttribute("hostingTeams", hostingTeams);
//
//            // 참여 중인 모임 조회 (승인된 참여자 상태)
//            List<Team> participatingTeams = teamService.getTeamsByParticipant(userId);
//            model.addAttribute("participatingTeams", participatingTeams);
//
//            // 참여한 모든 모임 조회 (승인된 참여자 및 상태 무관)
//            List<Team> allTeamsForUser = teamService.getAllTeamsForUser(userId);
//            model.addAttribute("allTeamsForUser", allTeamsForUser);
//
//            // 통계 데이터 조회 (UserController에서 처리하던 방식 참고)
//            Map<String, Integer> stats = new HashMap<>();
//            stats.put("hostingCount", hostingTeams.size());
//            stats.put("participatingCount", participatingTeams.size());
//            // restaurantCount는 TeamController에서 어떻게 계산할지 정의해야 함
//            stats.put("restaurantCount", 0);
//            model.addAttribute("stats", stats);
//
//            // 리뷰 정보 조회 (UserController에서 처리하던 방식 참고)
//            List<Team> teamsWithoutReview = new ArrayList<>();
//            List<Team> completedTeams = teamService.getTeamsByParticipant(userId).stream()
//                .filter(team -> team.getStatus() == Status.COMPLETED)
//                .toList();
//
//            for (Team team : completedTeams) {
//                boolean hasCompletedAllReviews = teamReviewService.hasUserCompletedAllReviews(team.getTeamId(), userId);
//                if (!hasCompletedAllReviews) {
//                    teamsWithoutReview.add(team);
//                }
//            }
//            model.addAttribute("teamsWithoutReview", teamsWithoutReview);
//
//            // 맛집 정보 (UserController에서 처리하던 방식 참고 - 필요하다면)
//            model.addAttribute("userMaps", new ArrayList<>());
//
//            return "user/mypage";
//
//        } catch (Exception e) {
//            log.error("TeamController - 마이페이지 (모임 정보) 로딩 중 오류 발생: {}", e.getMessage(), e);
//            return "redirect:/user/signin?error=profile_load_failed";
//        }
//    }



    // 모임 완료 후 리뷰 작성 페이지 표시
    @GetMapping("/{teamId}/reviews")
    public String showTeamReviewPage(@PathVariable Long teamId, Model model) {
        Team team = teamService.getTeamById(teamId);

        if (team == null) {
            return "redirect:/error/404";
        }

        if (team.getStatus() != Status.COMPLETED) {
            return "redirect:/team/" + teamId + "?error=notCompleted";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        Participant participant = participantRepository.findByUser_UserIdAndTeam_TeamId(
            currentUserId, teamId);
        if (participant == null) {
            return "redirect:/team/" + teamId + "?error=notParticipant";
        }

        List<Participant> participants = participantRepository.findParticipantsWithUserByTeamId(
            teamId);

        List<TeamReview> myReviews = teamReviewRepository.findByTeam_TeamIdAndReviewer(teamId,
            currentUserId);

        model.addAttribute("team", team);
        model.addAttribute("participants", participants);
        model.addAttribute("myReviews", myReviews);
        model.addAttribute("currentUserId", currentUserId);

        return "team/teamReview";
    }
}