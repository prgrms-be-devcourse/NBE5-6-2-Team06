package com.grepp.matnam.app.model.team;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.entity.QParticipant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParticipantRepositoryImpl implements ParticipantRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QParticipant participant = QParticipant.participant;

    @Override
    public long countApprovedExcludingHost(Long teamId, ParticipantStatus status) {
        Long count = queryFactory
            .select(participant.count())
            .from(participant)
            .where(
                participant.team.teamId.eq(teamId),
                participant.participantStatus.eq(status),
                participant.user.userId.ne(participant.team.user.userId)
            )
            .fetchOne();

        return count != null ? count : 0L;
    }

}
