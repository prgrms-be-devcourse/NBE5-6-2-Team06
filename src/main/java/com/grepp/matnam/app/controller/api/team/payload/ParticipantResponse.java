package com.grepp.matnam.app.controller.api.team.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponse {
    private Long participantId;
    private String userId;
    private String nickname;
    private String role;
    private String participantStatus;
    private float temperature;
}
