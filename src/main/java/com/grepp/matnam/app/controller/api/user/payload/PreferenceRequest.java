package com.grepp.matnam.app.controller.api.user.payload;

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
}