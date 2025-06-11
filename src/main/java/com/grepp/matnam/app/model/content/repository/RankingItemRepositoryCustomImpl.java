package com.grepp.matnam.app.model.content.repository;

import com.grepp.matnam.app.model.content.dto.QRankingItemSummaryDto;
import com.grepp.matnam.app.model.content.dto.RankingItemSummaryDto;
import com.grepp.matnam.app.model.content.entity.RankingItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.grepp.matnam.app.model.content.entity.QRankingItem.rankingItem;

@Repository
@RequiredArgsConstructor
public class RankingItemRepositoryCustomImpl implements RankingItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RankingItem> findActiveItemsByRanking(Long contentRankingId) {
        return queryFactory
                .selectFrom(rankingItem)
                .where(
                        rankingItem.contentRanking.id.eq(contentRankingId)
                                .and(rankingItem.isActive.isTrue())
                )
                .orderBy(rankingItem.ranking.asc())
                .fetch();
    }

    @Override
    public List<RankingItem> findAllItemsByRanking(Long contentRankingId) {
        return queryFactory
                .selectFrom(rankingItem)
                .where(rankingItem.contentRanking.id.eq(contentRankingId))
                .orderBy(rankingItem.ranking.asc())
                .fetch();
    }

    @Override
    public List<RankingItemSummaryDto> findItemSummariesByRanking(Long contentRankingId) {
        return queryFactory
                .select(new QRankingItemSummaryDto(
                        rankingItem.id,
                        rankingItem.ranking,
                        rankingItem.itemName,
                        rankingItem.isActive
                ))
                .from(rankingItem)
                .where(
                        rankingItem.contentRanking.id.eq(contentRankingId)
                                .and(rankingItem.isActive.isTrue())
                )
                .orderBy(rankingItem.ranking.asc())
                .fetch();
    }

    @Override
    public long deleteAllItemsByRanking(Long contentRankingId) {
        return queryFactory
                .delete(rankingItem)
                .where(rankingItem.contentRanking.id.eq(contentRankingId))
                .execute();
    }
}