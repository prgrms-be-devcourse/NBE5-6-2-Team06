package com.grepp.matnam.app.model.chat.entity;

import com.grepp.matnam.app.model.team.entity.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String name;

//    @Builder
//    public ChatRoom(String name, Team team) {
//        this.name = name;
//        this.team = team;
//    }
//
//    public static ChatRoom createRoom(String name, Team team) {
//        return ChatRoom.builder()
//            .name(name)
//            .team(team)
//            .build();
//    }
}

