package com.grepp.matnam.app.controller.api.admin.payload;

import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "평점은 최소 1점입니다.")
    @Max(value = 5, message = "평점은 최대 5점입니다.")
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

    @AssertTrue(message = "최소 1개에서 최대 3개의 분위기를 선택해주세요. ")
    public boolean isMoodSelectionCountValid() {
        int count = 0;

        if (goodTalk) count++;
        if (manyDrink) count++;
        if (goodMusic) count++;
        if (clean) count++;
        if (goodView) count++;
        if (isTerrace) count++;
        if (goodPicture) count++;
        if (goodMenu) count++;
        if (longStay) count++;
        if (bigStore) count++;

        return count >= 1 && count <= 3;
    }

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
