package com.grepp.matnam.app.controller.api.admin.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgeDistributionResponse {
    private String ageGroup; // 예: "20대", "30대"
    private long count;      // 해당 연령대 사용자 수
}
