package com.grepp.matnam.app.model.content.dto;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.entity.RankingItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "순위는 필수입니다")
    @Min(value = 1, message = "순위는 1 이상이어야 합니다")
    private Integer ranking;

    @NotBlank(message = "아이템명은 필수입니다")
    private String itemName;

    private String description;

    @NotNull(message = "활성화 상태는 필수입니다")
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