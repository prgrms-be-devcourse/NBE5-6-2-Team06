package com.grepp.matnam.app.controller.api.team.payload;

import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatedTeamRequest {

    private String title;
    private String description;
    private String date;
    private String time;
    private String category;
    private int maxPeople;
    private String restaurantName;
    private String restaurantAddress;
    private String imageUrl;

    public Team toTeam() {
        Team team = new Team();
        team.setTeamTitle(this.title);
        team.setTeamDetails(this.description);
        team.setMaxPeople(this.maxPeople);
        team.setCategory(this.category);
        team.setRestaurantName(this.restaurantName);
        team.setRestaurantAddress(this.restaurantAddress);
        team.setImageUrl(this.imageUrl);

        if (this.date != null && this.time != null) {
            String dateTimeString = this.date + "T" + this.time + ":00";
            try {
                team.setTeamDate(LocalDateTime.parse(dateTimeString));
            } catch (Exception e) {
                team.setTeamDate(LocalDateTime.now());
            }
        } else {
            team.setTeamDate(LocalDateTime.now());
        }

        team.setNowPeople(1);
        team.setStatus(Status.RECRUITING);

        return team;
    }
}
