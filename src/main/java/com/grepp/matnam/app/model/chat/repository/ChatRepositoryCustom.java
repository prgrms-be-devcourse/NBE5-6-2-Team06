package com.grepp.matnam.app.model.chat.repository;

import com.grepp.matnam.app.model.chat.entity.Chat;
import java.util.List;

public interface ChatRepositoryCustom {
    List<Chat> findByTeamIdOrderBySendDate(Long teamId);

}
