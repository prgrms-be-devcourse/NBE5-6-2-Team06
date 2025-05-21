package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {


    // userId와 teamId로 Participant 조회
    Participant findByUser_UserIdAndTeam_TeamId(String userId, Long teamId);

    boolean existsByUser_UserIdAndTeam_TeamId(String userId, Long teamId);

    List<Participant> findByTeam_TeamId(Long teamId);

    // 특정 팀에 참여한 사용자 조회
    List<Participant> findByUser_UserId(String userId);

    @Query("SELECT p FROM Participant p JOIN FETCH p.user WHERE p.team.teamId = :teamId")
    List<Participant> findParticipantsWithUserByTeamId(@Param("teamId") Long teamId);

    boolean existsByUser_UserIdAndTeam_TeamIdAndParticipantStatus(String userId, Long teamId, ParticipantStatus participantStatus);

    // 특정 팀에 속하고 상태가 승인 상태인 사용자 조회
    List<Participant> findByTeam_TeamIdAndParticipantStatus(Long teamId, ParticipantStatus participantStatus);

    @Query("""
       SELECT COUNT(p)
         FROM Participant p
        WHERE p.team.teamId      = :teamId
          AND p.participantStatus = :status
          AND p.user.userId       <> p.team.user.userId
    """)
    long countApprovedExcludingHost(
        @Param("teamId") Long teamId,
        @Param("status") ParticipantStatus status
    );

}
