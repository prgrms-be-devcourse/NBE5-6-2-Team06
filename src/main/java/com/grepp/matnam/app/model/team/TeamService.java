package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.controller.web.admin.payload.ActiveTeamResponse;
import com.grepp.matnam.app.controller.web.admin.payload.NewTeamResponse;
import com.grepp.matnam.app.model.chat.entity.ChatRoom;
import com.grepp.matnam.app.model.chat.repository.ChatRoomRepository;
import com.grepp.matnam.app.model.mymap.MymapRepository;
import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Role;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.dto.TeamDto;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserRepository;
import com.grepp.matnam.app.model.user.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;

    private final ParticipantRepository participantRepository;
    private final MymapRepository mymapRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 모임 생성
    public void saveTeam(Team team) {
        teamRepository.save(team);
        // 1. ChatRoom 생성
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTeam(team);         // ChatRoom → Team 연결
        chatRoom.setName(team.getTeamId()+"번 채팅방");
        chatRoomRepository.save(chatRoom);

    }

    // 참여자 추가
    @Transactional
    public void addParticipant(Long teamId, User user) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        Participant participant = new Participant();
        participant.setTeam(team);
        participant.setUser(user);
        participant.setParticipantStatus(ParticipantStatus.PENDING);

        if (team.getUser().equals(user)) {
            participant.setRole(Role.LEADER);  // 모임을 생성한 사용자는 LEADER
        } else {
            participant.setRole(Role.MEMBER);  // 나머지 사용자는 MEMBER
        }

        participantRepository.save(participant);
    }

    public void updateTeam(Long teamId, TeamDto teamDto) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        // 수정할 필드들 업데이트
        team.setTeamTitle(teamDto.getTeamTitle());
        team.setMeetDate(teamDto.getMeetDate());
        team.setRestaurantName(teamDto.getRestaurantName());
        team.setMaxPeople(teamDto.getMaxPeople());
        team.setNowPeople(teamDto.getNowPeople());

        // 팀 정보 저장
        teamRepository.save(team);
    }

//    public void deleteTeam(Long teamId) {
//        Team team = teamRepository.findById(teamId)
//            .orElseThrow(() -> new RuntimeException("Team not found"));
//
//        // 관련된 참가자 정보 삭제
//        participantRepository.deleteByTeam_TeamId(teamId);
//
//        // 팀 정보 삭제
//        teamRepository.delete(team);
//    }




    // 참여자 상태 변경
    public void changeParticipantStatus(Long participantId, ParticipantStatus status) {
        Participant participant = participantRepository.findById(participantId)
            .orElseThrow(() -> new RuntimeException("Participant not found")); //예외처리 수정하기
        participant.setParticipantStatus(status);
        participantRepository.save(participant);
    }

    // 모임 상태 변경
    public void changeTeamStatus(Long teamId, Status status) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found")); //예외처리 수정하기
        Status prevStatus = team.getStatus();
        team.setStatus(status);
        teamRepository.save(team);

        // 모임이 완료 상태가 되면 참여자들의 매너온도 증가
        if (status == Status.COMPLETED && prevStatus != Status.COMPLETED) {
            increaseTemperatureForCompletedTeam(team);
        }
    }


//    // 모임 참여 수락
//    @Transactional
//    public void acceptParticipant(Long teamId, String userId) {
//        Participant participant = participantRepository.findByUser_UserIdAndTeam_TeamId(userId, teamId);
//
//        if (participant == null) {
//            throw new RuntimeException("참가자가 존재하지 않거나 팀에 속하지 않습니다.");
//        }
//
//        // 이미 수락된 참가자는 상태 변경을 하지 않음
//        participant.setParticipantStatus(ParticipantStatus.APPROVED);
//        participantRepository.save(participant);
//    }

    // 팀 삭제
    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        participantRepository.deleteByTeam_TeamId(teamId);
        teamRepository.delete(team);
    }

    //조회 부분
    // 주최자로서의 팀 조회
    public List<Team> getTeamsByLeader(String userId) {
        return teamRepository.findTeamsByUser_UserId(userId);
    }

    //참여자로서의 팀 조회
//    public List<Team> getTeamsByParticipant(String userId) {
//        return teamRepository.findTeamsByParticipantUserId(userId);
//    }

    //참여자로서의 팀 조회 (APPROVED 상태)
    public List<Team> getTeamsByParticipant(String userId) {
        return teamRepository.findTeamsByParticipantUserIdAndParticipantStatus(userId, ParticipantStatus.APPROVED);
    }

    // 사용자의 모든 참여 정보 조회 (PENDING, APPROVED, REJECTED)
    public List<Participant> getAllParticipantsForUser(String userId) {
        return participantRepository.findByUser_UserId(userId);
    }

    // 사용자가 참여한 모든 모임 조회 (상태 무관)
    public List<Team> getAllTeamsForUser(String userId) {
        List<Participant> participants = getAllParticipantsForUser(userId);
        return participants.stream()
            .map(Participant::getTeam)
            .distinct()
            .collect(Collectors.toList());
    }

    // 참여자 조회(참여 목록)
    public List<Participant> getParticipant(Long teamId) {
        return participantRepository.findByTeam_TeamId(teamId);
    }


    // 참여자 상세 정보 조회(참여 상태)

    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
            .orElse(null);
    }

    public Participant getParticipantById(Long participantId) {
        return participantRepository.findById(participantId).orElse(null);
    }

    // 모임 검색 페이지
    public Page<Team> getAllTeams(Pageable pageable) {
        return teamRepository.findAllWithParticipants(pageable);
    }

    // 모임 상세 조회
    @Transactional
    public Team getTeamByIdWithParticipants(Long teamId) {
        return teamRepository.findByIdWithParticipantsAndUser(teamId).orElse(null);
    }

    // 모임 완료 처리
    @Transactional
    public void completeTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        Status prevStatus = team.getStatus();

        team.setStatus(Status.COMPLETED);
        teamRepository.save(team);

        if (prevStatus != Status.COMPLETED) {
            increaseTemperatureForCompletedTeam(team);
        }
    }

    private void increaseTemperatureForCompletedTeam(Team team) {
        List<Participant> participants = participantRepository.findByTeam_TeamId(team.getTeamId());

        for (Participant participant : participants) {
            if (participant.getParticipantStatus() == ParticipantStatus.APPROVED) {
                User user = participant.getUser();
                float currentTemp = user.getTemperature();
                float newTemp = currentTemp + 0.1f;

                user.setTemperature(newTemp);
                userRepository.save(user);
            }
        }
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Page<Team> findByFilter(String status, String keyword, Pageable pageable) {
        if (!status.isBlank() && StringUtils.hasText(keyword)) {
            return teamRepository.findByStatusAndKeywordContaining(status, keyword, pageable);
        } else if (!status.isBlank()) {
            return teamRepository.findByStatus(status, pageable);
        } else if (StringUtils.hasText(keyword)) {
            return teamRepository.findByKeywordContaining(keyword, pageable);
        } else {
            return teamRepository.findAllUsers(pageable);
        }
    }

    public List<Participant> findAllWithUserByTeamId(Long teamId) {
        return participantRepository.findParticipantsWithUserByTeamId(teamId);
    }

    @Transactional
    public void updateTeamStatus(Long teamId, Status status) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));
        team.setStatus(status);
    }

    @Transactional
    public void unActivatedById(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));
        log.info("team {}", team);
        team.unActivated();
    }

    public NewTeamResponse getNewTeamStats() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        long newTeams = teamRepository.countByCreatedAtBetween(today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        long totalTeamCount = teamRepository.count();
        long yesterdayTotalTeamCount = totalTeamCount - newTeams;
        String userGrowth = calculateGrowthRate(totalTeamCount, yesterdayTotalTeamCount);
        return new NewTeamResponse(newTeams, userGrowth);
    }

    public ActiveTeamResponse getActiveTeamStats() {

        List<Status> activeStatuses = List.of(Status.RECRUITING, Status.FULL);
        long todayActiveTeams = teamRepository.countByStatusIn(activeStatuses);
        long totalTeamCount = teamRepository.countAllByActivated(true);

        return new ActiveTeamResponse(todayActiveTeams, totalTeamCount);
    }

    private String calculateGrowthRate(long today, long yesterday) {
        if (yesterday == 0) {
            if (today == 0) return "+0%";
            return "+100%"; // 또는 "N/A"
        }
        long diff = today - yesterday;
        double percent = ((double) diff / yesterday) * 100;
        String sign = percent >= 0 ? "+" : "";
        return String.format("%s%.0f%%", sign, percent);
    }

    public List<Map<String, String>> getMonthlyMeetingSuccessRate() {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Map<String, Long>> monthlyStats = teamRepository.findMonthlyMeetingStats(sixMonthsAgo);

        return monthlyStats.stream().map(stat -> {
            Object monthObj = stat.get("meetingMonth");
            String month = String.valueOf(monthObj);

            Long total = stat.get("totalMeetings");
            Long completed = stat.get("completedMeetings");
            double successRate = (total > 0) ? ((double) completed / total) * 100 : 0;

            return Map.of(
                "month", month,
                "successRate", String.format("%.2f", successRate)
            );
        }).collect(Collectors.toList());
    }

    // 팀 참여자 조회 및 해당 유저별 맛집 목록 불러오기
    public List<Map<String, Object>> getParticipantMymapData(Long teamId) {
        List<Participant> participants = participantRepository.findParticipantsWithUserByTeamId(teamId);

        return participants.stream()
                .filter(p -> p.getParticipantStatus() == ParticipantStatus.APPROVED) // 승인된 참여자만
                .map(p -> {
                    User user = p.getUser();
                    List<Mymap> mymaps = mymapRepository.findByUserAndActivatedTrueAndPinnedTrue(user); // 맛집 필터링
                    List<Map<String, Object>> restaurants = mymaps.stream()
                            .map(m -> {
                                Map<String, Object> map = new HashMap<>();
                                map.put("mapId", m.getMapId());
                                map.put("name", m.getPlaceName());
                                map.put("roadAddress", m.getRoadAddress());
                                map.put("latitude", m.getLatitude());
                                map.put("longitude", m.getLongitude());
                                map.put("memo", m.getMemo());
                                return map;
                            })
                            .toList();
                    return Map.of(
                            "userId", user.getUserId(),
                            "nickname", user.getNickname(),
                            "restaurants", restaurants
                    );
                })
                .toList();
    }
    
    public Map<String, Integer> getUserStats(String userId) {
        Map<String, Integer> stats = new HashMap<>();

        List<Team> leaderTeams = getTeamsByLeader(userId);
        stats.put("leaderCount", leaderTeams.size());

        List<Team> participatingTeams = getTeamsByParticipant(userId);
        participatingTeams.removeAll(leaderTeams);
        stats.put("participatingCount", participatingTeams.size());

        List<Team> completedTeams = getAllTeamsForUser(userId).stream()
                .filter(team -> team.getStatus() == Status.COMPLETED)
                .collect(Collectors.toList());
        stats.put("completedCount", completedTeams.size());

        return stats;
    }

    // 모임 수정
//    public TeamDto getTeamDetails(Long teamId) {
//    }
}
