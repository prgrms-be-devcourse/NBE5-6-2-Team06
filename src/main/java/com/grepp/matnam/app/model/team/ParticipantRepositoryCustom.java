package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;

public interface ParticipantRepositoryCustom {
    long countApprovedExcludingHost(Long teamId, ParticipantStatus status);

}
