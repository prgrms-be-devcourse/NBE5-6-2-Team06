package com.grepp.matnam.app.controller.api.team.payload;

import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequest {

    private String title;
    private String description;

    private String date;
    private String time;

    private String category;

    private int maxPeople;
    private int nowPeople;

    private String restaurantName;
    private String restaurantAddress;

    public Team toDto(User user) {
        Team team = new Team();
        team.setUser(user);
        team.setTeamTitle(this.title);
        team.setTeamDetails(this.description);
        team.setMaxPeople(this.maxPeople);
        team.setCategory(this.category);
        team.setNowPeople(1);
        team.setStatus(Status.RECRUITING);
        team.setRestaurantName(this.restaurantName);
        team.setRestaurantAddress(this.restaurantAddress);
        return team;
    }

}


