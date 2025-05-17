package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Status;
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
public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom{

    // 사용자 ID로 팀 조회 (주최자)
    List<Team> findTeamsByUser_UserId(String userId);

    @Query("SELECT t FROM Team t JOIN t.participants p WHERE p.user.userId = :userId AND p.participantStatus = :participantStatus")
    List<Team> findTeamsByParticipantUserIdAndParticipantStatus(@Param("userId") String userId, @Param("participantStatus") ParticipantStatus participantStatus);

    // 사용자 모임 조회
    //@Query("SELECT t FROM Team t JOIN t.participants p WHERE p.user.userId = :userId")

    //List<Team> findTeamsByParticipantUserId(@Param("userId") String userId);


    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.participants ORDER BY t.createdAt DESC")
    Page<Team> findAllWithParticipants(Pageable pageable);

    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.participants p LEFT JOIN FETCH p.user WHERE t.teamId = :teamId")
    Optional<Team> findByIdWithParticipantsAndUser(@Param("teamId") Long teamId);

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
}
