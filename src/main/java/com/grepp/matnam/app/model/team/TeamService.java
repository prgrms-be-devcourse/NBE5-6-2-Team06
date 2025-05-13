package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Role;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserRepository;
import com.grepp.matnam.app.model.user.entity.User;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Status prevStatus = team.getStatus();
        team.setStatus(status);
        teamRepository.save(team);

        // 모임이 완료 상태가 되면 참여자들의 매너온도 증가
        if (status == Status.COMPLETED && prevStatus != Status.COMPLETED) {
            increaseTemperatureForCompletedTeam(team);
        }
    }


    // 모임 참여 수락
    @Transactional
    public void acceptParticipant(Long teamId, String userId) {
        Participant participant = participantRepository.findByUser_UserIdAndTeam_TeamId(userId, teamId);

        if (participant == null) {
            throw new RuntimeException("참가자가 존재하지 않거나 팀에 속하지 않습니다.");
        }

        if (participant.getParticipantStatus() == ParticipantStatus.APPROVED) {
            throw new RuntimeException("참가자는 이미 수락되었습니다.");
        }

        participant.setParticipantStatus(ParticipantStatus.APPROVED);
        participantRepository.save(participant);
    }



    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        List<Participant> participants = participantRepository.findByTeam_TeamId(teamId);
        if (participants != null && !participants.isEmpty()) {
            participantRepository.deleteAll(participants);
        }

        teamRepository.delete(team);
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
        return teamRepository.findTeamsByParticipants_User_UserId(userId);
    }

    // 특정 사용자와 팀 ID로 참여자 조회
    public List<Participant> getParticipant(Long teamId) {
        return participantRepository.findByTeam_TeamId(teamId);
    }



    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElse(null);
    }

    public Participant getParticipantById(Long participantId) {
        return participantRepository.findById(participantId).orElse(null);
    }


    public Page<Team> getAllTeams(Pageable pageable) {
        return teamRepository.findAllWithParticipants(pageable);
    }

    public Team getTeamByIdWithParticipants(Long teamId) {
        return teamRepository.findByIdWithParticipants(teamId).orElse(null);
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

}
