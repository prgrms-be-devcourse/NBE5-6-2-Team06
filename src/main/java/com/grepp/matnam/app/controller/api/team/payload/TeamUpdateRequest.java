package com.grepp.matnam.app.controller.api.team.payload;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;


@Data
public class TeamUpdateRequest {

    @NotNull(message = "모임 제목은 필수입니다.")
    private String teamTitle;

    @NotNull(message = "모임 정보")
    private String teamDetails;

    @NotNull(message = "모임 장소는 필수입니다.")
    private String restaurantName;

    @NotNull(message = "모임 날짜는 필수입니다.")
    private LocalDateTime teamDate;

    @NotNull(message = "모임 시간은 필수입니다.")
    private LocalDateTime meetDate;

    @NotNull(message = "최대 인원은 필수입니다.")
    private Integer maxPeople;

    private String category;

    private String imageUrl;


}
