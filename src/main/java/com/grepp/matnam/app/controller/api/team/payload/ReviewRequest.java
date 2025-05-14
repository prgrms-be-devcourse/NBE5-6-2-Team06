package com.grepp.matnam.app.controller.api.team.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private Long teamId;
    private String reviewerId;
    private String revieweeId;
    private Double rating;
}