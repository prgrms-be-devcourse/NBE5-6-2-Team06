package com.grepp.matnam.app.model.chat;

import com.grepp.matnam.app.model.chat.dto.MessageDto;
import com.grepp.matnam.app.model.chat.entity.Chat;
import com.grepp.matnam.app.model.chat.entity.ChatRoom;
import com.grepp.matnam.app.model.chat.repository.ChatRepository;
import com.grepp.matnam.app.model.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Long saveChatMessage(MessageDto message) {
        log.info("Saving message for teamId: {}", message.getTeamId());
        ChatRoom chatRoom = chatRoomRepository.findByTeam_TeamId(message.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        Chat chat = Chat.createChat(
                chatRoom,
                message.getSenderNickname(),
                message.getSenderId(),
                message.getMessage()
        );
        Chat savedChat = chatRepository.save(chat);
        log.info("메시지 저장 완료, chatId: {}", savedChat.getChatId());

        return savedChat.getChatId();
    }
    @Transactional(readOnly = true)
    public List<MessageDto> getChatHistory(Long teamId) {
        log.debug("Getting chat history for teamId: {}", teamId);
        List<Chat> chatList = chatRepository.findByTeamIdOrderBySendDate(teamId);

        return chatList.stream()
                .map(chat -> new MessageDto(chat, teamId))
                .collect(Collectors.toList());
    }

}


