package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.chat.entity.Chat;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportChatResponse {

    private String senderNickname;
    private String senderId;
    private String message;
    private String sendDate;

    public ReportChatResponse(Chat chat) {
        this.senderNickname = chat.getSenderNickname();
        this.senderId = chat.getSenderId();
        this.message = chat.getMessage();
        this.sendDate = chat.getSendDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }
}
