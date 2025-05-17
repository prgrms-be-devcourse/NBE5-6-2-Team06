package com.grepp.matnam.app.model.chat.dto;

import com.grepp.matnam.app.model.chat.entity.Chat;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDto {
    private Long chatId;
    private Long teamId;
    private String senderId;
    private String senderNickname;
    private String message;
    private String time;

    public MessageDto(Chat chat, Long teamId) {
        this.chatId = chat.getChatId();
        this.teamId = teamId;
        this.senderId = chat.getSenderId();
        this.senderNickname = chat.getSenderNickname();
        this.message = chat.getMessage();
        this.time = chat.getSendDate().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
