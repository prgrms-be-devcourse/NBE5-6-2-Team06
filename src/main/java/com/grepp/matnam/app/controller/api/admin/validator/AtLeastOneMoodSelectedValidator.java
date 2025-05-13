package com.grepp.matnam.app.controller.api.admin.validator;

import com.grepp.matnam.app.controller.api.admin.annotation.AtLeastOneMoodSelected;
import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneMoodSelectedValidator implements
    ConstraintValidator<AtLeastOneMoodSelected, RestaurantRequest> {
    @Override
    public boolean isValid(RestaurantRequest request, ConstraintValidatorContext context) {
        return request.isGoodTalk() ||
            request.isManyDrink() ||
            request.isGoodMusic() ||
            request.isClean() ||
            request.isGoodView() ||
            request.isTerrace() ||
            request.isGoodPicture() ||
            request.isGoodMenu() ||
            request.isLongStay() ||
            request.isBigStore();
    }
}
