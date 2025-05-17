package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String summary;

    @NotBlank
    private String address;

    @NotBlank
    private String openTime;

    @NotBlank
    private String tel;

    @NotBlank
    private String mainFood;

    @NotBlank(message = "카테고리를 선택해주세요.")
    private String category;

    private String mood;

    @NotNull(message = "평점을 입력해주세요.")
    private Float googleRating;

    private boolean goodTalk;
    private boolean manyDrink;
    private boolean goodMusic;
    private boolean clean;
    private boolean goodView;
    private boolean isTerrace;
    private boolean goodPicture;
    private boolean goodMenu;
    private boolean longStay;
    private boolean bigStore;

    public Restaurant toEntity() {
        Restaurant restaurant = new Restaurant();

        restaurant.setName(name);
        restaurant.setCategory(category);
        restaurant.setAddress(address);
        restaurant.setTel(tel);
        restaurant.setOpenTime(openTime);
        restaurant.setMainFood(mainFood);
        restaurant.setSummary(summary);
        restaurant.setGoodTalk(goodTalk);
        restaurant.setManyDrink(manyDrink);
        restaurant.setGoodMusic(goodMusic);
        restaurant.setClean(clean);
        restaurant.setGoodView(goodView);
        restaurant.setTerrace(isTerrace);
        restaurant.setGoodPicture(goodPicture);
        restaurant.setGoodMenu(goodMenu);
        restaurant.setLongStay(longStay);
        restaurant.setBigStore(bigStore);
        restaurant.setGoogleRating(googleRating);

        return restaurant;
    }
}
