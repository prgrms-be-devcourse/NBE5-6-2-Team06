package com.grepp.matnam.app.model.content.repository;

import com.grepp.matnam.app.model.content.dto.RankingItemSummaryDto;
import com.grepp.matnam.app.model.content.entity.RankingItem;

import java.util.List;

public interface RankingItemRepositoryCustom {

    List<RankingItem> findActiveItemsByRanking(Long contentRankingId);

    List<RankingItem> findAllItemsByRanking(Long contentRankingId);

    List<RankingItemSummaryDto> findItemSummariesByRanking(Long contentRankingId);

    long deleteAllItemsByRanking(Long contentRankingId);
}