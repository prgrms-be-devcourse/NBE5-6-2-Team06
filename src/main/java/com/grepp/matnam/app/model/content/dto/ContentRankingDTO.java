package com.grepp.matnam.app.model.content.dto;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private String subtitle;

    @NotNull(message = "시작일은 필수입니다")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "활성화 상태는 필수입니다")
    private Boolean isActive;

    @Valid
    @NotEmpty(message = "랭킹 아이템은 최소 1개 이상이어야 합니다")
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