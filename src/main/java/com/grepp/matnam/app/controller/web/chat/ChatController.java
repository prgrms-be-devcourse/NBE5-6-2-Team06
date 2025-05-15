package com.grepp.matnam.app.controller.web.chat;

import com.grepp.matnam.app.model.chat.ChatService;
import com.grepp.matnam.app.model.chat.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    // 구독 채널들에게 메시지 전송을 위한
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/message")
    public void sendMessage(MessageDto message) {
        Long teamId = message.getTeamId();

        chatService.saveChatMessage(message);
        System.out.println("==========DB 저장완료===========");

        messagingTemplate.convertAndSend("/room/" + teamId, message);
        System.out.println("======/room/" + teamId+"메시지 전송 완료======");


    }
}

