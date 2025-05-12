package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Role;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;

    private final ParticipantRepository participantRepository;

    private final UserRepository userRepository;

    // 모임 생성
    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    // 참여자 추가
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
        team.setStatus(status);
        teamRepository.save(team);
    }


    // 모임 참여 수락
    public void acceptParticipant(Long teamId, Long userId) {
        // 참가자 정보 가져오기
        Participant participant = participantRepository.findTeamsByUserIdAndTeamId(teamId, userId);
        if (participant != null) {
            participant.setParticipantStatus(ParticipantStatus.APPROVED);
            participantRepository.save(participant);
        }
    }

    public void deleteTeam(Long teamId) {

    }

    public Team getTeamById(Long teamId) {
        return null;
    }


    // 사용자가 참여한 모임 조회 (참여 완료 및 참여 중 포함)
    public Iterable<Team> getUserTeams(Long userId) {
        // 사용자가 주최한 모임
        List<Team> hostedTeams = teamRepository.findTeamsByUserId(userId);

        // 사용자가 참여한 모든 모임
        List<Team> participatedTeams = participantRepository.findTeamsByUserId(userId);

        hostedTeams.addAll(participatedTeams);
        return hostedTeams;
    }


    // 주최 중 모임 조회
    public Iterable<Team> getTeamsByLeader(Long userId) {
        return teamRepository.findTeamsByUserId(userId);
    }

    // 참여 중 모임 조회
    public Iterable<Team> getTeamsByParticipant(Long userId) {
        return participantRepository.findTeamsByParticipantStatusAndUserId(
            ParticipantStatus.PENDING, userId);
    }

    // 사용자 정보 조회
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
