package com.grepp.matnam.app.model.chat.repository;

import com.grepp.matnam.app.model.chat.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByTeam_TeamId(Long teamId);


}
