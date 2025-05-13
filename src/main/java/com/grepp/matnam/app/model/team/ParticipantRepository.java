package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {


    // userId와 teamId로 Participant 조회
    Participant findByUser_UserIdAndTeam_TeamId(String userId, Long teamId);
}
