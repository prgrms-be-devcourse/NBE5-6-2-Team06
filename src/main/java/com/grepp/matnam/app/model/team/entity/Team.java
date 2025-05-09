package com.grepp.matnam.app.model.team.entity;

import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamId")
    private List<Participant> participants;

    private String teamTitle;

    private String teamDetails;

    private LocalDateTime teamDate;

    private LocalDateTime meetDate;

    private Integer maxPeople;

    private Integer nowPeople;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String imageUrl;

    private String restaurantName;

}