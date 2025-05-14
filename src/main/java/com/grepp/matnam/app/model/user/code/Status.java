package com.grepp.matnam.app.model.user.code;

public enum Status {
    ACTIVE("정상"),       // 정상
    SUSPENDED("정지"),    // 정지 (일시적)
    BANNED("영구 정지");        // 영구 정지

    private final String koreanName;

    Status(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}
