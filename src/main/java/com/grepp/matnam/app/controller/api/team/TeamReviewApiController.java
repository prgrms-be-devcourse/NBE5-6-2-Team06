package com.grepp.matnam.app.controller.api.team;

import com.grepp.matnam.app.controller.api.team.payload.ParticipantResponse;
import com.grepp.matnam.app.controller.api.team.payload.ReviewRequest;
import com.grepp.matnam.app.controller.api.team.payload.ReviewSummary;
import com.grepp.matnam.app.controller.api.team.payload.TeamReviewResponse;
import com.grepp.matnam.app.model.team.TeamReviewService;
import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.team.entity.TeamReview;
import com.grepp.matnam.app.model.user.UserRepository;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review API", description = "모임 리뷰 관리 API")
public class TeamReviewApiController {

    private final TeamReviewService teamReviewService;
    private final UserRepository userRepository;

    @PostMapping
    @Operation(summary = "리뷰 작성", description = "모임 참여자에 대한 리뷰를 작성합니다.")
    public ResponseEntity<ApiResponse> createReview(@Validated @RequestBody ReviewRequest request) {
        try {
            String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!currentUserId.equals(request.getReviewerId())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "리뷰 작성 실패", "리뷰 작성자 ID가 일치하지 않습니다."));
            }

            teamReviewService.createReview(
                    request.getTeamId(),
                    request.getReviewerId(),
                    request.getRevieweeId(),
                    request.getRating()
            );

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "리뷰 작성 성공", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "리뷰 작성 실패", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "리뷰 작성 중 오류가 발생했습니다.", e.getMessage()));
        }
    }

    @GetMapping("/my-reviews")
    @Operation(summary = "내가 작성한 리뷰 목록 조회", description = "특정 모임에서 내가 작성한 리뷰 목록을 조회합니다.")
    public ResponseEntity<ApiResponse> getMyReviews(@RequestParam Long teamId) {
        try {
            String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

            List<TeamReview> reviews = teamReviewService.getReviewsByTeamAndReviewer(teamId, currentUserId);

            List<TeamReviewResponse> responses = reviews.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "내 리뷰 목록 조회 성공", responses));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "리뷰 목록 조회 중 오류가 발생했습니다.", e.getMessage()));
        }
    }

    @GetMapping("/reviews-about-me")
    @Operation(summary = "나에 대한 리뷰 목록 조회", description = "특정 모임에서 나에 대해 작성된 리뷰 목록을 조회합니다.")
    public ResponseEntity<ApiResponse> getReviewsAboutMe(@RequestParam Long teamId) {
        try {
            String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

            List<TeamReview> reviews = teamReviewService.getReviewsByTeamAndReviewee(teamId, currentUserId);

            List<TeamReviewResponse> responses = reviews.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "나에 대한 리뷰 목록 조회 성공", responses));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "리뷰 목록 조회 중 오류가 발생했습니다.", e.getMessage()));
        }
    }

    @GetMapping("/teams/{teamId}/participants")
    @Operation(summary = "모임 참여자 목록 조회", description = "특정 모임의 참여자 목록을 조회합니다.")
    public ResponseEntity<ApiResponse> getTeamParticipants(@PathVariable Long teamId) {
        try {
            List<Participant> participants = teamReviewService.getTeamParticipants(teamId);

            List<ParticipantResponse> responses = participants.stream()
                    .map(this::convertToParticipantResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "모임 참여자 목록 조회 성공", responses));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "참여자 목록 조회 중 오류가 발생했습니다.", e.getMessage()));
        }
    }

    @GetMapping("/summary")
    @Operation(summary = "리뷰 요약 정보 조회", description = "특정 모임의 리뷰 요약 정보를 조회합니다.")
    public ResponseEntity<ApiResponse> getReviewSummary(
            @RequestParam Long teamId,
            @RequestParam(required = false) String userId) {
        try {
            String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
            String targetUserId = userId != null ? userId : currentUserId;

            TeamReviewService.ReviewSummaryInfo summaryInfo =
                    teamReviewService.getReviewSummaryInfo(teamId, targetUserId);

            ReviewSummary summary = new ReviewSummary();
            summary.setTeamId(teamId);
            summary.setUserId(targetUserId);
            summary.setAverageRating(summaryInfo.getAverageRating());
            summary.setReceivedReviewCount(summaryInfo.getReceivedReviewCount());
            summary.setTemperatureChange(summaryInfo.getTemperatureChange());

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "리뷰 요약 정보 조회 성공", summary));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "리뷰 요약 정보 조회 중 오류가 발생했습니다.", e.getMessage()));
        }
    }

    @GetMapping("/completion-status")
    @Operation(summary = "리뷰 완료 상태 확인", description = "사용자가 특정 모임의 모든 참여자에 대한 리뷰를 작성했는지 확인합니다.")
    public ResponseEntity<ApiResponse> checkReviewCompletionStatus(@RequestParam Long teamId) {
        try {
            String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

            boolean isComplete = teamReviewService.hasUserCompletedAllReviews(teamId, currentUserId);

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "리뷰 완료 상태 확인 성공", isComplete));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "리뷰 완료 상태 확인 중 오류가 발생했습니다.", e.getMessage()));
        }
    }

    private TeamReviewResponse convertToResponse(TeamReview review) {
        TeamReviewResponse response = new TeamReviewResponse();
        response.setTeamReviewId(review.getTeamReviewId());
        response.setTeamId(review.getTeam().getTeamId());
        response.setReviewerId(review.getReviewer());
        response.setRevieweeId(review.getReviewee());
        response.setRating(review.getRating());
        response.setCreatedAt(review.createdAt());

        User reviewer = userRepository.findById(review.getReviewer()).orElse(null);
        if (reviewer != null) {
            response.setReviewerNickname(reviewer.getNickname());
        }

        User reviewee = userRepository.findById(review.getReviewee()).orElse(null);
        if (reviewee != null) {
            response.setRevieweeNickname(reviewee.getNickname());
        }

        return response;
    }

    private ParticipantResponse convertToParticipantResponse(Participant participant) {
        ParticipantResponse response = new ParticipantResponse();
        response.setParticipantId(participant.getParticipantId());
        response.setUserId(participant.getUser().getUserId());
        response.setNickname(participant.getUser().getNickname());
        response.setRole(participant.getRole().name());
        response.setParticipantStatus(participant.getParticipantStatus().name());
        response.setTemperature(participant.getUser().getTemperature());

        return response;
    }
}