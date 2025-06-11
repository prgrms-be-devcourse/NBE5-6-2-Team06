package com.grepp.matnam.app.controller.api.team.payload;

import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequest {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다.")
    private String title;
    @NotBlank(message = "설명은 필수 입력 값입니다.")
    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;

    @NotBlank(message = "날짜는 필수 입력 값입니다.")
    private String date;

    @NotBlank(message = "시간은 필수 입력 값입니다.")
    private String time;

    private String category;
    private Integer maxPeople;
    private Integer nowPeople;

    @NotBlank(message = "식당 이름은 필수 입력 값입니다.")
    private String restaurantName;

    @NotBlank(message = "식당 주소는 필수 입력 값입니다.")
    private String restaurantAddress;

    private String imageUrl;

    public Team toEntity(User user) {
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
        team.setImageUrl(this.imageUrl);
        return team;
    }

}


