package com.grepp.matnam.app.controller.api.team.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSummary {
    private Long teamId;
    private String userId;
    private Double averageRating;
    private long receivedReviewCount;
    private float temperatureChange;
}