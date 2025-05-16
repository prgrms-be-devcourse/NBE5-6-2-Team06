package com.grepp.matnam.app.model.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantDto {

    private int restaurantId;
    private String restaurantName;
    private String mainFood;
    private String description;
    private String category;
    private String address;
    private String openTime;

    public RestaurantDto(String name, String description, String address, String category ,String openTime, String mainFood) {
        this.restaurantName = name;
        this.description = description;
        this.address = address;
        this.category = category;
        this.openTime = openTime;
        this.mainFood = mainFood;
    }
}
