package com.grepp.matnam.app.model.content.dto;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.entity.RankingItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingItemDTO {
    private Long id;
    private Long contentRankingId;
    private Integer ranking;
    private String itemName;
    private String description;
    private Boolean isActive;

    public static RankingItemDTO from(RankingItem entity) {
        return RankingItemDTO.builder()
                .id(entity.getId())
                .contentRankingId(entity.getContentRanking().getId())
                .ranking(entity.getRanking())
                .itemName(entity.getItemName())
                .description(entity.getDescription())
                .isActive(entity.getIsActive())
                .build();
    }

    public RankingItem toEntity(ContentRanking contentRanking) {
        return RankingItem.builder()
                .id(id)
                .contentRanking(contentRanking)
                .ranking(ranking)
                .itemName(itemName)
                .description(description)
                .isActive(isActive != null ? isActive : true)
                .build();
    }
}