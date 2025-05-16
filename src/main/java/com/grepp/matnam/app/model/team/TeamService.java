package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.controller.web.admin.payload.ActiveTeamResponse;
import com.grepp.matnam.app.controller.web.admin.payload.NewTeamResponse;
import com.grepp.matnam.app.controller.api.team.payload.TeamUpdateRequest;
import com.grepp.matnam.app.model.chat.entity.ChatRoom;
import com.grepp.matnam.app.model.chat.repository.ChatRoomRepository;
import com.grepp.matnam.app.model.preference.repository.PreferenceRepository;
import com.grepp.matnam.app.model.restaurant.RestaurantRepository;
import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Role;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.dto.TeamDto;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserRepository;
import com.grepp.matnam.app.model.user.entity.Preference;
import com.grepp.matnam.app.model.user.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final PreferenceRepository preferenceRepository;
//    private final RestaurantRepository restaurantRepository;

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
            .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        if (!participantRepository.existsByUser_UserIdAndTeam_TeamId(user.getUserId(), teamId)) {
            // 이미 참가한 여부 파악 -> 예외처리
            Participant participant = new Participant();
            participant.setTeam(team);
            participant.setUser(user);

            if(user.getUserId().equals(team.getUser().getUserId())) {
                participant.setParticipantStatus(ParticipantStatus.APPROVED);
                participant.setRole(Role.LEADER);

                if (team.getNowPeople() == null || team.getNowPeople() == 0) {
                    team.setNowPeople(1);
                }
            } else { // 일반 사용자
                participant.setParticipantStatus(ParticipantStatus.PENDING);
                participant.setRole(Role.MEMBER);
            }

            participantRepository.save(participant);
        } else {
            throw new IllegalStateException("이미 참여한 사용자입니다.");
        }
    }

    // 모임 참여 수락
    @Transactional
    public void approveParticipant(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
            .orElseThrow(() -> new RuntimeException("참가자를 찾을 수 없습니다."));

        if (participant.getParticipantStatus() == ParticipantStatus.APPROVED) {
            throw new RuntimeException("이미 수락된 참가자입니다.");
        }

        Team team = participant.getTeam();
        if (team.getMaxPeople() != null && team.getNowPeople() >= team.getMaxPeople()) {
            throw new RuntimeException("모임의 최대 인원 수를 초과했습니다.");
        }

        participant.setParticipantStatus(ParticipantStatus.APPROVED);
        participantRepository.save(participant);

//        team.setNowPeople(team.getNowPeople() + 1);
        if (team.getNowPeople() == null) {
            team.setNowPeople(1);
        } else {
            team.setNowPeople(team.getNowPeople() + 1);
        }
        teamRepository.save(team);
    }

//    // 수정한 필드 저장
//    @Transactional
//    public Team updateTeam(Long teamId, TeamUpdateRequest request) {
//        // 팀 찾기
//        Team team = teamRepository.findById(teamId)
//            .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));
//
//        // 필드 수정
//        if (request.getTeamTitle() != null) {
//            team.setTeamTitle(request.getTeamTitle());
//        }
//        if (request.getTeamDetails() != null) {
//            team.setTeamDetails(request.getTeamDetails());
//        }
//        if (request.getRestaurantName() != null) {
//            team.setRestaurantName(request.getRestaurantName());
//        }
//        if (request.getTeamDate() != null) {
//            team.setMeetDate(request.getTeamDate());
//        }
//        if (request.getMeetDate() != null) {
//            team.setMeetDate(request.getMeetDate());
//        }
//        if (request.getCapacity() != null) {
//            team.setMaxPeople(request.getCapacity());
//        }
//
//        if (request.getCategory() != null && team.getRestaurant() != null) {
//            Restaurant restaurant = team.getRestaurant();
//            restaurant.setCategory(request.getCategory());
//            restaurantRepository.save(restaurant); // Restaurant 정보 업데이트
//        }
//        if (request.getImageUrl() != null) {
//            team.setImageUrl(request.getImageUrl());
//        }
//
//        // 수정된 팀 정보 저장
//        return teamRepository.save(team);
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
    // 특정 팀의 사용자 teamId를 통해 userId 리스트 받기[상태가 승인인 사용자]
    public List<Participant> getApprovedUserIdsByTeamId(Long teamId) {
        return participantRepository.findByTeam_TeamIdAndParticipantStatus(teamId, ParticipantStatus.APPROVED);
    }

    //팀원들의 취향 키워드 종합
    public List<String> countPreferenceKeyword(Long teamId) {
        //집계 시작
        List<Participant> approvedParticipants = getApprovedUserIdsByTeamId(teamId);

        if (approvedParticipants.isEmpty()) {
            throw new RuntimeException("사용자가 없습니다.");
        } else {
            //집계 값을 받을 Map
            Map<String, Integer> keywordCount = new HashMap<>();

            keywordCount.put("goodTalk", 0);
            keywordCount.put("manyDrink", 0);
            keywordCount.put("goodMusic", 0);
            keywordCount.put("clean", 0);
            keywordCount.put("goodView", 0);
            keywordCount.put("isTerrace", 0);
            keywordCount.put("goodPicture", 0);
            keywordCount.put("goodMenu", 0);
            keywordCount.put("longStay", 0);
            keywordCount.put("bigStore", 0);

            //집계 핵심
            for (Participant participant : approvedParticipants) {
                User user = participant.getUser();
                Preference pref = preferenceRepository.findByUser(user);

                if (pref.isGoodTalk()) {
                    keywordCount.put("goodTalk", keywordCount.get("goodTalk") + 1);
                }
                if (pref.isManyDrink()) {
                    keywordCount.put("manyDrink", keywordCount.get("manyDrink") + 1);
                }
                if (pref.isGoodMusic()) {
                    keywordCount.put("goodMusic", keywordCount.get("goodMusic") + 1);
                }
                if (pref.isClean()) {
                    keywordCount.put("clean", keywordCount.get("clean") + 1);
                }
                if (pref.isGoodView()) {
                    keywordCount.put("goodView", keywordCount.get("goodView") + 1);
                }
                if (pref.isTerrace()) {
                    keywordCount.put("isTerrace", keywordCount.get("isTerrace") + 1);
                }
                if (pref.isGoodPicture()) {
                    keywordCount.put("goodPicture", keywordCount.get("goodPicture") + 1);
                }
                if (pref.isGoodMenu()) {
                    keywordCount.put("goodMenu", keywordCount.get("goodMenu") + 1);
                }
                if (pref.isLongStay()) {
                    keywordCount.put("longStay", keywordCount.get("longStay") + 1);
                }
                if (pref.isBigStore()) {
                    keywordCount.put("bigStore", keywordCount.get("bigStore") + 1);
                }

            }
            // 최대값 찾기
            int max = keywordCount.values().stream().max(Integer::compareTo).orElse(0);

            // 최댓값 키워드
            List<String> topKeywords = keywordCount.entrySet().stream().
                filter(entry -> entry.getValue() == max).
                map(Map.Entry::getKey).
                toList();

            System.out.println(topKeywords);
            return topKeywords;

        }
    }

    // 모임 수정
//    public TeamDto getTeamDetails(Long teamId) {
//    }
}
