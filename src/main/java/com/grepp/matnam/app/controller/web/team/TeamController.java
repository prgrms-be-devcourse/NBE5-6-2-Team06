package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.controller.api.team.payload.TeamRequest;
import com.grepp.matnam.app.controller.api.team.payload.UpdatedTeamRequest;
import com.grepp.matnam.app.model.team.ParticipantRepository;
import com.grepp.matnam.app.model.team.TeamReviewRepository;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Role;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.team.entity.TeamReview;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/team")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Team API", description = "모임 관련 REST API")
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

    // 모임 생성
    @PostMapping("/create")
    public String createTeam(@ModelAttribute TeamRequest teamRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        User user = userService.getUserById(userId);
        Team team = teamRequest.toDto(user);

        if (teamRequest.getDate() != null && teamRequest.getTime() != null) {
            String dateTimeString = teamRequest.getDate() + "T" + teamRequest.getTime() + ":00";
            try {
                team.setTeamDate(LocalDateTime.parse(dateTimeString));
            } catch (Exception e) {
                team.setTeamDate(LocalDateTime.now());
            }
        } else {
            team.setTeamDate(LocalDateTime.now());
        }

        teamService.saveTeam(team);
        teamService.addParticipant(team.getTeamId(), user);

        return "redirect:/team/detail/" + team.getTeamId();
    }

    // 모임 수정 페이지
    @GetMapping("/edit/{teamId}")
    public String getTeamEditPage(@PathVariable Long teamId, Model model) {
        Team team = teamService.getTeamById(teamId);
        model.addAttribute("team", team);
        return "team/teamEdit";
    }

    // 모임 수정
    @PostMapping("/edit/{teamId}")
    public String updateTeam(@PathVariable Long teamId, @ModelAttribute UpdatedTeamRequest updatedTeamRequest) {
        Team team = updatedTeamRequest.toTeam();
        team.setTeamId(teamId);

        // 업데이트 처리
        teamService.updateTeam(teamId, team);
        return "redirect:/team/detail/" + teamId;
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
    @Operation(summary = "특정 모임 정보 조회 (REST API)", description = "주어진 ID의 모임 정보를 REST API로 조회")
    @GetMapping("/detail/{teamId}")
    public String teamDetail(@PathVariable Long teamId, Model model) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        Team team = teamService.getTeamByIdWithParticipants(teamId);
        model.addAttribute("team", team);

        boolean isLeader = team.getUser().getUserId().equals(userId);
        model.addAttribute("isLeader", isLeader);

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