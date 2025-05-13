package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.team.entity.TeamReview;
import com.grepp.matnam.app.model.user.UserRepository;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamReviewService {

    private final TeamReviewRepository teamReviewRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    public TeamReview createReview(Long teamId, String reviewerId, String revieweeId, Double rating) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        boolean isReviewerInTeam = participantRepository.existsByUser_UserIdAndTeam_TeamId(
                reviewerId, teamId);
        boolean isRevieweeInTeam = participantRepository.existsByUser_UserIdAndTeam_TeamId(
                revieweeId, teamId);

        if (!isReviewerInTeam || !isRevieweeInTeam) {
            throw new IllegalArgumentException("리뷰어나 리뷰이가 같은 팀에 속해있지 않습니다.");
        }

        if (reviewerId.equals(revieweeId)) {
            throw new IllegalArgumentException("자신에게 리뷰를 작성할 수 없습니다.");
        }

        boolean isDuplicate = teamReviewRepository.existsByTeam_TeamIdAndReviewerAndReviewee(
                teamId, reviewerId, revieweeId);

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 리뷰를 작성했습니다.");
        }

        if (team.getStatus() != Status.COMPLETED) {
            throw new IllegalArgumentException("완료된 모임에 대해서만 리뷰를 작성할 수 있습니다.");
        }

        TeamReview review = new TeamReview();
        review.setTeam(team);
        review.setReviewer(reviewerId);
        review.setReviewee(revieweeId);
        review.setRating(rating);

        TeamReview savedReview = teamReviewRepository.save(review);

        updateTemperatureByReview(revieweeId, rating);

        return savedReview;
    }

    private void updateTemperatureByReview(String userId, Double rating) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        float currentTemp = user.getTemperature();
        float adjustment = 0;

        if (rating >= 4.0) {
            adjustment = 0.3f;
        } else if (rating >= 3.0) {
            adjustment = 0.1f;
        } else {
            adjustment = -0.3f;
        }

        user.setTemperature(currentTemp + adjustment);
        userRepository.save(user);
    }

    public List<TeamReview> getReviewsByTeamAndReviewer(Long teamId, String reviewerId) {
        return teamReviewRepository.findByTeam_TeamIdAndReviewer(teamId, reviewerId);
    }

    public List<TeamReview> getReviewsByTeamAndReviewee(Long teamId, String revieweeId) {
        return teamReviewRepository.findByTeam_TeamIdAndReviewee(teamId, revieweeId);
    }

    public List<Participant> getTeamParticipants(Long teamId) {
        return participantRepository.findByTeam_TeamId(teamId);
    }

    // 특정 팀의 모든 참여자가 리뷰를 마쳤는지 확인 -> 모임 페이지에 리뷰 작성 버튼을 보여주기 위한 메서드
    public boolean areAllReviewsCompleted(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        List<Participant> participants = team.getParticipants();
        int participantCount = participants.size();

        int expectedReviewCount = participantCount * (participantCount - 1);

        long actualReviewCount = teamReviewRepository.countByTeam_TeamId(teamId);

        return actualReviewCount >= expectedReviewCount;
    }

    public boolean hasUserCompletedAllReviews(Long teamId, String userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        List<Participant> participants = team.getParticipants();
        int otherParticipantCount = (int) participants.stream()
                .filter(p -> !p.getUser().getUserId().equals(userId))
                .count();

        long reviewedCount = teamReviewRepository.findByTeam_TeamIdAndReviewer(teamId, userId).size();

        return reviewedCount >= otherParticipantCount;
    }

    public ReviewSummaryInfo getReviewSummaryInfo(Long teamId, String userId) {
        Double avgRating = teamReviewRepository.calculateAverageRatingByTeamAndReviewee(teamId, userId);

        long receivedReviewCount = teamReviewRepository.countByTeam_TeamIdAndReviewee(teamId, userId);

        float temperatureChange = calculateTemperatureChangeFromReviews(teamId, userId);

        return new ReviewSummaryInfo(
                avgRating != null ? avgRating : 0.0,
                receivedReviewCount,
                temperatureChange
        );
    }

    private float calculateTemperatureChangeFromReviews(Long teamId, String userId) {
        List<TeamReview> reviews = teamReviewRepository.findByTeam_TeamIdAndReviewee(teamId, userId);
        float change = 0.0f;

        for (TeamReview review : reviews) {
            double rating = review.getRating();
            if (rating >= 4.0) {
                change += 0.3f;
            } else if (rating >= 3.0) {
                change += 0.1f;
            } else {
                change -= 0.3f;
            }
        }

        Team team = teamRepository.findById(teamId).orElse(null);
        if (team != null && team.getStatus() == Status.COMPLETED) {
            change += 0.1f;
        }

        return change;
    }

    @Getter
    public static class ReviewSummaryInfo {
        private final Double averageRating;
        private final long receivedReviewCount;
        private final float temperatureChange;

        public ReviewSummaryInfo(Double averageRating, long receivedReviewCount, float temperatureChange) {
            this.averageRating = averageRating;
            this.receivedReviewCount = receivedReviewCount;
            this.temperatureChange = temperatureChange;
        }
    }
}