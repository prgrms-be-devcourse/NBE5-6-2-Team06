package com.grepp.matnam.app.model.content.dto;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentRankingDTO {
    private Long id;
    private String title;
    private String subtitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private List<RankingItemDTO> items;

    public static ContentRankingDTO from(ContentRanking entity) {
        return ContentRankingDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .subtitle(entity.getSubtitle())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isActive(entity.getIsActive())
                .build();
    }

    public ContentRanking toEntity() {
        return ContentRanking.builder()
                .id(id)
                .title(title)
                .subtitle(subtitle)
                .startDate(startDate)
                .endDate(endDate)
                .isActive(isActive != null ? isActive : true)
                .build();
    }
}