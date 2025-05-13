package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // 사용자 ID로 팀 조회 (주최자)
    List<Team> findTeamsByUser_UserId(String userId);

    // 참여자가 userId인 팀 조회
    List<Team> findTeamsByParticipants_User_UserId(String userId);


    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.participants ORDER BY t.createdAt DESC")
    Page<Team> findAllWithParticipants(Pageable pageable);

    // 특정 ID의 팀과 participants를 함께 로딩 (Fetch Join) - 상세보기 등에 활용 가능
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.participants WHERE t.teamId = :teamId")
    Optional<Team> findByIdWithParticipants(Long teamId);
}
