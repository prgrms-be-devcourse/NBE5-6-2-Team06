package com.grepp.matnam.app.controller.api.team.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TeamReviewResponse {
    private Long teamReviewId;
    private Long teamId;
    private String reviewerId;
    private String reviewerNickname;
    private String revieweeId;
    private String revieweeNickname;
    private Double rating;
    private LocalDateTime createdAt;
}
