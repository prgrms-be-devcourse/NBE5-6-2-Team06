package com.grepp.matnam.app.controller.api.team.payload;

import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.entity.User;
import java.time.LocalDateTime;
import lombok.Data;
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
    private int capacity;

    private String restaurantName;
    private String restaurantAddress;

    public Team toDto(User user){
        Team team = new Team();
        team.setUser(user);
        team.setTeamTitle(this.title);
        team.setTeamDetails(this.description);
        team.setMeetDate(LocalDateTime.now());
        team.setTeamDate(LocalDateTime.now());
        team.setMaxPeople(this.capacity);
        team.setStatus(Status.RECRUITING);
        team.setRestaurantName(this.restaurantName);
        return team;
    }

}


