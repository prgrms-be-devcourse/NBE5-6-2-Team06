package com.grepp.matnam.app.controller.api.admin.payload;

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

    @NotBlank(message = "최소 1개 이상의 분위기를 선택해주세요.")
    private String mood;

    @NotNull(message = "평점을 입력해주세요.")
    private Float googleRating;

    @NotNull(message = "평점을 입력해주세요.")
    private Float naverRating;

    @NotNull(message = "평점을 입력해주세요.")
    private Float kakaoRating;
}
