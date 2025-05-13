package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.entity.Participant;
import java.util.List;
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

    @Query("SELECT p FROM Participant p JOIN FETCH p.user WHERE p.team.teamId = :teamId")
    List<Participant> findParticipantsWithUserByTeamId(@Param("teamId") Long teamId);

    // 모든 팀과 그 participants를 페이징 처리하여 로딩 (Fetch Join)

}
