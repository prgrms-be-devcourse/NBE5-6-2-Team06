package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.controller.api.team.payload.TeamRequest;
import com.grepp.matnam.app.model.team.ParticipantRepository;
import com.grepp.matnam.app.model.team.TeamReviewRepository;
import com.grepp.matnam.app.model.team.TeamReviewService;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Role;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.dto.TeamDto;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.team.entity.TeamReview;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
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
    private final TeamReviewRepository teamReviewRepository;
    private final ParticipantRepository participantRepository;


    // 모임 생성 페이지
    @GetMapping("/create")
    public String create() {
        return "team/teamCreate";
    }

    // 모임 수정
//    @GetMapping("/edit/{teamId}")
//    public String getTeamEditPage(@PathVariable Long teamId, Model model) {
//        TeamDto teamDto = teamService.getTeamDetails(teamId);
//        model.addAttribute("teamDto", teamDto);
//        return "team/teamCreate";
//    }


    // 모임 생성
    @PostMapping("/create")
    public String createTeam(@ModelAttribute TeamRequest teamRequest, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        User user = userService.getUserById(userId);
        Team team = teamRequest.toDto(user);

        if (teamRequest.getDate() != null && teamRequest.getTime() != null) {
            String dateTimeString = teamRequest.getDate() + "T" + teamRequest.getTime() + ":00";
            try {
                team.setMeetDate(LocalDateTime.parse(dateTimeString));
            } catch (Exception e) {
                team.setMeetDate(LocalDateTime.now());
            }
        } else {
            team.setMeetDate(LocalDateTime.now());
        }

        team.setTeamDate(LocalDateTime.now());

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
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        Team team = teamService.getTeamByIdWithParticipants(teamId);
        model.addAttribute("team", team);

        boolean isLeader = team.getUser().getUserId().equals(userId);
        model.addAttribute("isLeader", isLeader);  // 방장 여부를 모델에 추가

        // 현재 사용자가 이미 팀에 참여했는지
        boolean isParticipant = participantRepository.existsByUser_UserIdAndTeam_TeamIdAndParticipantStatus(
            userId, teamId, ParticipantStatus.APPROVED);
        // 현재 사용자가 이미 신청했는지
        boolean alreadyApplied = participantRepository.existsByUser_UserIdAndTeam_TeamIdAndParticipantStatus(
            userId, teamId, ParticipantStatus.PENDING);

        // approved 상태인 참가자들만 가져와서 리스트로 변환 - 뷰로 사용
        List<Participant> approvedParticipants = team.getParticipants().stream()
            .filter(participant -> participant.getParticipantStatus() == ParticipantStatus.APPROVED)
            .toList();
        model.addAttribute("participants", approvedParticipants);

        model.addAttribute("isParticipant", isParticipant);
        model.addAttribute("alreadyApplied", alreadyApplied);
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
            .filter(participant -> participant.getRole() == Role.MEMBER)
            .toList();
        model.addAttribute("participants", approvedParticipants);

        return "team/teamPage";
    }

    // 모임 참여 신청
    @PostMapping("/{teamId}/apply")
    public String applyToJoinTeam(@PathVariable Long teamId, @RequestParam String userId) {
        User user = userService.getUserById(userId);

        teamService.addParticipant(teamId, user);
//        return "redirect:/team/" + teamId + "/page";
        return "redirect:/team/detail/" + teamId;
    }

    // 승인 처리
    @PostMapping("/{teamId}/approve")
    public ResponseEntity<Team> approveParticipant(@PathVariable Long teamId, @RequestParam String userId) {
        try {
            teamService.approveParticipant(teamId, userId);
            Team team = teamService.getTeamById(teamId);
            return ResponseEntity.ok(team);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


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