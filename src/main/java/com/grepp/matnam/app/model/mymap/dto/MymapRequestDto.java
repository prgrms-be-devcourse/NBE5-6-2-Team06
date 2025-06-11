package com.grepp.matnam.app.model.mymap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MymapRequestDto {

    @NotBlank(message = "장소명은 필수입니다.")
    private String placeName;

    @NotBlank(message = "도로명 주소는 필수입니다.")
    private String roadAddress;

    @NotNull(message = "위도 값은 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도 값은 필수입니다.")
    private Double longitude;

    private String memo;

    @NotNull(message = "핀 여부는 필수입니다.")
    private Boolean pinned;
}
