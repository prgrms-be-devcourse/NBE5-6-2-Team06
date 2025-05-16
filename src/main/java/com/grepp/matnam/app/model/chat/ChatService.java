package com.grepp.matnam.app.model.chat;

import com.grepp.matnam.app.model.chat.dto.MessageDto;
import com.grepp.matnam.app.model.chat.entity.Chat;
import com.grepp.matnam.app.model.chat.entity.ChatRoom;
import com.grepp.matnam.app.model.chat.repository.ChatRepository;
import com.grepp.matnam.app.model.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Long saveChatMessage(MessageDto message) {
        System.out.println(">>> [SERVICE] Saving message for teamId: " + message.getTeamId());
        ChatRoom chatRoom = chatRoomRepository.findByTeam_TeamId(message.getTeamId())
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Chat chat = Chat.createChat(
                chatRoom,
                message.getSenderNickname(),
                message.getSenderId(),
                message.getMessage()
        );
        Chat savedChat = chatRepository.save(chat);
        System.out.println("메시지 저장 완료, chatId: " + savedChat.getChatId());

        return savedChat.getChatId();
    }

    public List<MessageDto> getChatHistory(Long teamId) {
        System.out.println("teamId: " + teamId);
        List<Chat> chatList = chatRepository.findByTeamIdOrderBySendDate(teamId);

        return chatList.stream()
                .map(chat -> new MessageDto(chat, teamId))
                .collect(Collectors.toList());
    }

}


