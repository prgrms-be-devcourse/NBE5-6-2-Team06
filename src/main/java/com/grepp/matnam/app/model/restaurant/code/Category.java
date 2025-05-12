package com.grepp.matnam.app.model.restaurant.code;


public enum Category {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    BUNSIK("분식"),
    DESSERT("디저트");

    private final String koreanName;

    Category(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }

    @Override
    public String toString() {
        return koreanName;
    }
}