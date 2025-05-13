package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Role;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserRepository;
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
    public void acceptParticipant(Long teamId, String userId) {
        // 참가자 정보 가져오기
        Participant participant = participantRepository.findByUser_UserIdAndTeam_TeamId(userId, teamId);
        if (participant != null) {
            participant.setParticipantStatus(ParticipantStatus.APPROVED);
            participantRepository.save(participant);
        } else {
            throw new RuntimeException("참가자가 존재하지 않거나 이미 수락되었습니다."); // 예외 수정하기
        }
    }


    public void deleteTeam(Long teamId) {

    }


    // userId로 속한 팀 목록 조회
    public List<Team> getUserTeams(String userId) {
        return teamRepository.findTeamsByUser_UserId(userId);
    }

    // 주최자로서의 팀 조회
    public List<Team> getTeamsByLeader(String userId) {
        return teamRepository.findTeamsByUser_UserId(userId);
    }

    // 참여자로서의 팀 조회
    public List<Team> getTeamsByParticipant(String userId) {
        return teamRepository.findTeamsByParticipants_User_UserId(userId);  // 수정된 메서드 사용
    }

    // 특정 사용자와 팀 ID로 참여자 조회
    public Participant getParticipant(String userId, Long teamId) {
        return participantRepository.findByUser_UserIdAndTeam_TeamId(userId, teamId);
    }


    public Team getTeamById(Long teamId) {
        return null;
    }

    public Participant getParticipantById(Long participantId) {
        return participantRepository.findById(participantId).orElse(null);
    }

}
