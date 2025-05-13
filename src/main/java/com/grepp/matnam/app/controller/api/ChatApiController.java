package com.grepp.matnam.app.controller.api;

import com.grepp.matnam.app.model.chat.ChatService;
import com.grepp.matnam.app.model.chat.dto.MessageDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    @GetMapping("/api/chat/history/{roomId}")
    public List<MessageDto> getChatHistory(@PathVariable Long roomId) {
        return chatService.getChatHistory(roomId);
    }
}

