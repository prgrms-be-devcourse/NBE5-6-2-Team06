package com.grepp.matnam.app.model.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ParticipantWithUserIdDto {
    private Long participantId;
    private String userId;
}
