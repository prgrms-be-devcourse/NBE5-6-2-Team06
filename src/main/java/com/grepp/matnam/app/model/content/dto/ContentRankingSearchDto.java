package com.grepp.matnam.app.model.content.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ContentRankingSearchDto {
    private Long id;
    private String title;
    private String subtitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private long itemCount;
    private LocalDateTime createdAt;

    @QueryProjection
    public ContentRankingSearchDto(Long id, String title, String subtitle,
                                   LocalDate startDate, LocalDate endDate,
                                   Boolean isActive, long itemCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.itemCount = itemCount;
        this.createdAt = createdAt;
    }
}