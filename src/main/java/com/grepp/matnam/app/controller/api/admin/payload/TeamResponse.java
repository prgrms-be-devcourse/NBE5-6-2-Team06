package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.team.entity.Team;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamResponse {
    private Long teamId;
    private String teamTitle;
    private String restaurantName;
    private String imageUrl;
    private String teamDate;
    private String teamTime;
    private LocalDateTime meetDate;
    private int maxPeople;
    private int nowPeople;
    private String status;
    private String statusKoreanName;
    private String organizerUserId;
    private String teamDetails;
    private String category;
    private String address;

    public TeamResponse(Team team) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        this.teamId = team.getTeamId();
        this.teamTitle = team.getTeamTitle();
        this.restaurantName = team.getRestaurantName();
        this.imageUrl = team.getImageUrl();
        this.teamDate = team.getTeamDate().format(dateFormatter);
        this.teamTime = team.getTeamDate().format(timeFormatter);
        this.maxPeople = team.getMaxPeople();
        this.nowPeople = team.getNowPeople();
        this.status = team.getStatus().name();
        this.statusKoreanName = team.getStatus().getKoreanName();
        this.organizerUserId = team.getUser().getUserId();
        this.teamDetails = team.getTeamDetails();
        this.address = team.getRestaurantAddress();
        this.category = team.getCategory();
    }
}


