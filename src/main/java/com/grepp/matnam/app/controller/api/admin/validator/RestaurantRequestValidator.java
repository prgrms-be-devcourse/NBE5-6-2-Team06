package com.grepp.matnam.app.controller.api.admin.validator;

import com.grepp.matnam.app.controller.api.admin.payload.RestaurantRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RestaurantRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RestaurantRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RestaurantRequest request = (RestaurantRequest) target;

        boolean hasMood = request.isGoodTalk()
            || request.isManyDrink()
            || request.isGoodMusic()
            || request.isClean()
            || request.isGoodView()
            || request.isTerrace()
            || request.isGoodPicture()
            || request.isGoodMenu()
            || request.isLongStay()
            || request.isBigStore();

        if (!hasMood) {
            errors.rejectValue("mood", "mood", "최소 1개 이상의 분위기를 선택해주세요.");
        }
    }
}
