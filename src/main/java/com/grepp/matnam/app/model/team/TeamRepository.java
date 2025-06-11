package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {

    // 사용자 ID로 팀 조회 (주최자)
    List<Team> findTeamsByUser_UserIdAndActivatedTrue(String userId);

    Optional<Team> findByTeamIdAndActivatedTrue(Long teamId);

    long countAllByActivated(Boolean activated);

    long countByCreatedAtBetweenAndActivatedTrue(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    long countByActivatedTrue();

    long countByStatusInAndActivatedTrue(List<Status> activeStatuses);

    long countByCreatedAtAfterAndActivatedTrue(LocalDateTime createdAtAfter);

    Long countByNowPeopleBetweenAndActivatedTrue(Integer nowPeopleAfter, Integer nowPeopleBefore);
}
