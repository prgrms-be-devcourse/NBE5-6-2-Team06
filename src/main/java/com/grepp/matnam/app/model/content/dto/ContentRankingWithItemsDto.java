package com.grepp.matnam.app.model.content.dto;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentRankingWithItemsDto {
    private ContentRanking ranking;
    private List<RankingItemSummaryDto> items;

    public static ContentRankingWithItemsDto of(ContentRanking ranking, List<RankingItemSummaryDto> items) {
        return new ContentRankingWithItemsDto(ranking, items);
    }
}