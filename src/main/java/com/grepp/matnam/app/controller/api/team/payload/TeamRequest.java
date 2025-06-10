package com.grepp.matnam.app.controller.api.team.payload;

import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;

    @NotBlank
    private String date;
    @NotBlank
    private String time;

    @NotBlank
    private String category;

    @NotBlank
    private int maxPeople;
    @NotBlank
    private int nowPeople;

    @NotBlank
    private String restaurantName;
    @NotBlank
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


