package com.grepp.matnam.app.model.team.code;

import lombok.Getter;

@Getter
public enum Status {
    RECRUITING("모집 중"),
    FULL("모집 마감"),
    COMPLETED("완료"),
    CANCELED("취소");

    private final String koreanName;

    Status(String koreanName) {
        this.koreanName = koreanName;
    }

}

