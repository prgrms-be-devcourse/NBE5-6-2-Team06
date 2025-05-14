package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.entity.TeamReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamReviewRepository extends JpaRepository<TeamReview, Long> {
    List<TeamReview> findByTeam_TeamIdAndReviewer(Long teamId, String reviewerId);

    List<TeamReview> findByTeam_TeamIdAndReviewee(Long teamId, String revieweeId);

    boolean existsByTeam_TeamIdAndReviewerAndReviewee(Long teamId, String reviewerId, String revieweeId);

    // 특정 팀의 특정 사용자에 대한 평균 평점 조회
    @Query("SELECT AVG(r.rating) FROM TeamReview r WHERE r.team.teamId = :teamId AND r.reviewee = :revieweeId")
    Double calculateAverageRatingByTeamAndReviewee(Long teamId, String revieweeId);

    long countByTeam_TeamId(Long teamId);

    long countByTeam_TeamIdAndReviewee(Long teamId, String revieweeId);
}