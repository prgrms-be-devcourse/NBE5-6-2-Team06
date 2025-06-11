package com.grepp.matnam.app.controller.api.user.payload;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class PreferenceRequest {
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

    @AssertTrue(message = "최소 4개 이상의 선호도를 선택해야 합니다.")
    public boolean isAtLeastFourPreferencesSelected() {
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

        return count >= 4;
    }
}