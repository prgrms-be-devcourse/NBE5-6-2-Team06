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

    public RestaurantDto(String name, String mainFood, String description, String category , String address,String openTime) {
        this.restaurantName = name;
        this.mainFood = mainFood;
        this.description = description;
        this.category = category;
        this.address = address;
        this.openTime = openTime;
    }
}
