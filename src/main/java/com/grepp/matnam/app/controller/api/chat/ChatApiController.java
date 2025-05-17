package com.grepp.matnam.app.controller.api.chat;

import com.grepp.matnam.app.model.chat.ChatService;
import com.grepp.matnam.app.model.chat.dto.MessageDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat")
public class ChatApiController {

    private final ChatService chatService;

    @GetMapping("/history/{roomId}")
    public List<MessageDto> getChatHistory(@PathVariable Long roomId) {
        return chatService.getChatHistory(roomId);
    }
}

