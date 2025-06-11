package com.grepp.matnam.app.model.content.repository;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.entity.RankingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingItemRepository extends JpaRepository<RankingItem, Long>, RankingItemRepositoryCustom {

    List<RankingItem> findByContentRankingAndIsActiveTrueOrderByRankingAsc(ContentRanking contentRanking);

    List<RankingItem> findByContentRankingOrderByRankingAsc(ContentRanking contentRanking);

    void deleteByContentRanking(ContentRanking contentRanking);
}