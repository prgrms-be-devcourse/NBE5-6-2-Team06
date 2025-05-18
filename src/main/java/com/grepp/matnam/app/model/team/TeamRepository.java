package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.dto.ParticipantWithUserIdDto;
import com.grepp.matnam.app.model.team.entity.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {

    // 사용자 ID로 팀 조회 (주최자)
    List<Team> findTeamsByUser_UserIdAndActivatedTrue(String userId);

    @Query("SELECT t FROM Team t JOIN t.participants p " +
            "WHERE p.user.userId = :userId AND p.participantStatus = :participantStatus AND t.activated = true")
    List<Team> findTeamsByParticipantUserIdAndParticipantStatusAndActivatedTrue(
            @Param("userId") String userId,
            @Param("participantStatus") ParticipantStatus participantStatus
    );

    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.participants WHERE t.activated = true ORDER BY t.createdAt DESC")
    Page<Team> findAllWithParticipantsAndActivatedTrue(Pageable pageable);

    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.participants p LEFT JOIN FETCH p.user " +
            "WHERE t.teamId = :teamId AND t.activated = true")
    Optional<Team> findByIdWithParticipantsAndUserAndActivatedTrue(@Param("teamId") Long teamId);

    Optional<Team> findByTeamIdAndActivatedTrue(Long teamId);

    long countByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);

    long countByStatusIn(List<Status> activeStatuses);

    long countAllByActivated(Boolean activated);

    @Query("SELECT " +
        "FUNCTION('DATE_FORMAT', t.teamDate, '%Y-%m') AS meetingMonth, " +
        "COUNT(t) AS totalMeetings, " +
        "SUM(CASE WHEN t.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completedMeetings " +
        "FROM Team t " +
        "WHERE t.teamDate >= :startDate " +
        "GROUP BY FUNCTION('DATE_FORMAT', t.teamDate, '%Y-%m') " +
        "ORDER BY FUNCTION('DATE_FORMAT', t.teamDate, '%Y-%m')")
    List<Map<String, Long>> findMonthlyMeetingStats(@Param("startDate") LocalDateTime startDate);

    Long countByNowPeopleBetween(int start, int end);

    long countByStatus(Status status);
    long countByCreatedAtAfter(LocalDateTime dateTime);

    @Query("SELECT AVG(t.maxPeople) FROM Team t WHERE t.status IN ('RECRUITING', 'FULL')")
    Double averageMaxPeopleForActiveTeams();

    @Query("SELECT new com.grepp.matnam.app.model.team.dto.ParticipantWithUserIdDto(p.participantId, u.userId) " +
        "FROM Participant p JOIN p.user u " +
        "WHERE p.team.teamId = :teamId AND p.team.activated = true")
    List<ParticipantWithUserIdDto> findAllDtoByTeamId(@Param("teamId") Long teamId);
}
