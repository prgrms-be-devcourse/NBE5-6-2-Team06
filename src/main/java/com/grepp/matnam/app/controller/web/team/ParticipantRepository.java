package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Iterable<Team> findTeamsByParticipantStatusAndUserId(ParticipantStatus participantStatus, Long userId);

    Participant findTeamsByUserIdAndTeamId(Long teamId, Long userId);

    List<Team> findTeamsByUserId(Long userId);
}
