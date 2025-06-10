package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.dto.MonthlyMeetingStatsDto;
import com.grepp.matnam.app.model.team.dto.ParticipantWithUserIdDto;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.dto.MonthlyMeetingStatsDto;
import com.grepp.matnam.app.model.team.dto.ParticipantWithUserIdDto;
import com.grepp.matnam.app.model.team.entity.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamRepositoryCustom {
    Page<Team> findAllUsers(Pageable pageable);

    Page<Team> findByStatusAndKeywordContaining(String status, String keyword, Pageable pageable);

    Page<Team> findByStatus(String status, Pageable pageable);

    Page<Team> findByKeywordContaining(String keyword, Pageable pageable);

    List<MonthlyMeetingStatsDto> findMonthlyMeetingStats(LocalDateTime startDate);

    double averageMaxPeopleForActiveTeams();

    List<ParticipantWithUserIdDto> findAllDtoByTeamId(Long teamId);

    List<Team> findTeamsByParticipantUserIdAndParticipantStatusAndActivatedTrue(String userId, ParticipantStatus status);

    Page<Team> findAllWithParticipantsAndActivatedTrue(Pageable pageable);

    Optional<Team> findByIdWithParticipantsAndUserAndActivatedTrue(Long teamId);

    List<MonthlyMeetingStatsDto> findMonthlyMeetingStats(LocalDateTime startDate);

    double averageMaxPeopleForActiveTeams();

    List<ParticipantWithUserIdDto> findAllDtoByTeamId(Long teamId);
}
