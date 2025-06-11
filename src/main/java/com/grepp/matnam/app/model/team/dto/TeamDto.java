package com.grepp.matnam.app.model.team.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDto {
    private String teamTitle;
    private String teamDetails;
    private LocalDateTime teamDate;
    private String restaurantName;
    private String restaurantAddress;
    private Integer maxPeople;
    private Integer nowPeople;
    private String category;
    private String imageUrl;

}
