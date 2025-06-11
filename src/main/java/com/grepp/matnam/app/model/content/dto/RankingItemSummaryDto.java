package com.grepp.matnam.app.model.content.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RankingItemSummaryDto {
    private Long id;
    private Integer ranking;
    private String itemName;
    private Boolean isActive;

    @QueryProjection
    public RankingItemSummaryDto(Long id, Integer ranking, String itemName, Boolean isActive) {
        this.id = id;
        this.ranking = ranking;
        this.itemName = itemName;
        this.isActive = isActive;
    }
}