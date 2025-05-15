package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.chat.entity.ChatRoom;
import com.grepp.matnam.app.model.chat.repository.ChatRoomRepository;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Role;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.dto.TeamDto;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserRepository;
import com.grepp.matnam.app.model.user.entity.User;
import java.util.List;

import jakarta.transaction.Transactional;
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
//    @Transactional
//    public void addParticipant(Long teamId, User user) {
//        Team team = teamRepository.findById(teamId)
//            .orElseThrow(() -> new RuntimeException("Team not found"));
//
//        Participant participant = new Participant();
//        participant.setTeam(team);
//        participant.setUser(user);
//        participant.setParticipantStatus(ParticipantStatus.PENDING);
//
//        if (team.getUser().equals(user)) {
//            participant.setRole(Role.LEADER);  // 모임을 생성한 사용자는 LEADER
//        } else {
//            participant.setRole(Role.MEMBER);  // 나머지 사용자는 MEMBER
//        }
//
//        participantRepository.save(participant);
//    }

    @Transactional
    public void addParticipant(Long teamId, User user) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        if (!participantRepository.existsByUser_UserIdAndTeam_TeamId(user.getUserId(), teamId)) {
            Participant participant = new Participant();
            participant.setTeam(team);
            participant.setUser(user);
            participant.setRole(Role.MEMBER);

            if (team.getUser().equals(user)) {
                participant.setParticipantStatus(ParticipantStatus.APPROVED);
                participant.setRole(Role.LEADER);
                team.setNowPeople(1);
            } else {
                participant.setParticipantStatus(ParticipantStatus.PENDING);
            }

            participantRepository.save(participant);
        } else {
            throw new IllegalStateException("이미 참여한 사용자입니다.");
        }
    }

    // 수정한 필드 저장
    public void updateTeam(Long teamId, TeamDto teamDto) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        team.setTeamTitle(teamDto.getTeamTitle());
        team.setMeetDate(teamDto.getMeetDate());
        team.setRestaurantName(teamDto.getRestaurantName());
        team.setMaxPeople(teamDto.getMaxPeople());
        team.setNowPeople(teamDto.getNowPeople());

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
    // 모임 수정
//    public TeamDto getTeamDetails(Long teamId) {
//    }
}
