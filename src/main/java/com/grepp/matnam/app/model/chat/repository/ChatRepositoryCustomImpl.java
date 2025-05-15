package com.grepp.matnam.app.model.chat.repository;

import com.grepp.matnam.app.model.chat.entity.Chat;
import com.grepp.matnam.app.model.chat.entity.QChat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // Qchat QueryDSL 에서 쿼리를 타입 안전하게 작성하기 위한 도구
    private final QChat chat = QChat.chat;

    @Override
    public List<Chat> findByTeamIdOrderBySendDate(Long teamId) {
        return queryFactory
            .selectFrom(chat)
            .where(chat.room.team.teamId.eq(teamId))
            .orderBy(chat.sendDate.asc())
            .fetch();
    }
}
