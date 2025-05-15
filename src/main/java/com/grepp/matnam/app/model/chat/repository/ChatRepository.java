package com.grepp.matnam.app.model.chat.repository;

import com.grepp.matnam.app.model.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {


}
