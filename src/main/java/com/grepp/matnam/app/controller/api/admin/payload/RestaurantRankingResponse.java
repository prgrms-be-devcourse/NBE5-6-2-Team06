package com.grepp.matnam.app.controller.api.admin.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRankingResponse {
    private String name;
    private Integer recommendedCount;
}
