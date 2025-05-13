package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // 사용자 ID로 팀 조회 (주최자)
    List<Team> findTeamsByUser_UserId(String userId);

    // 참여자가 userId인 팀 조회
    List<Team> findTeamsByParticipants_User_UserId(String userId);


}
