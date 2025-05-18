package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.code.Role;
import com.grepp.matnam.app.model.team.entity.Participant;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParticipantResponse {

    private Long participantId;
    private String userId;
    private String nickname;
    private String email;
    private Role role;
    private ParticipantStatus participantStatus;
    private String createdDate;

    public ParticipantResponse(Participant p) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        this.participantId = p.getParticipantId();
        this.userId = p.getUser().getUserId();
        this.nickname = p.getUser().getNickname();
        this.email = p.getUser().getEmail();
        this.role = p.getRole();
        this.participantStatus = p.getParticipantStatus();
        this.createdDate = p.getCreatedAt().format(dateFormatter);
    }

}