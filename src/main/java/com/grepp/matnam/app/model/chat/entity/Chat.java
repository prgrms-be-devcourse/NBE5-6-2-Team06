package com.grepp.matnam.app.model.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    //누가 보냈는지
    private String senderNickname;
    private String senderId;

    //메시지 내용
    private String message;

    //메시지 생성 시간
    @CreatedDate
    private LocalDateTime sendDate;

    //Builder 패턴으로 객체 생성
    @Builder
    public Chat(ChatRoom room,String senderNickname, String senderId, String message) {
        this.room = room;
        this.senderNickname = senderNickname;
        this.senderId = senderId;
        this.message = message;
        this.sendDate = LocalDateTime.now();
    }
    //정적 팩토리 메서드 객체 생성
    public static Chat createChat(ChatRoom room, String senderNickname, String senderId, String message) {
        return Chat.builder()
            .room(room)
            .senderNickname(senderNickname)
            .senderId(senderId)
            .message(message)
            .build();
    }

}
