package com.grepp.matnam.app.model.content.service;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.entity.RankingItem;
import com.grepp.matnam.app.model.content.repository.ContentRankingRepository;
import com.grepp.matnam.app.model.content.repository.RankingItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentRankingService {

    private final ContentRankingRepository contentRankingRepository;
    private final RankingItemRepository rankingItemRepository;

    public List<ContentRanking> getTodayActiveRankings() {
        return contentRankingRepository.findActiveRankingsForToday();
    }

    public Optional<ContentRanking> getCurrentActiveRanking() {
        return contentRankingRepository.findFirstActiveRankingForToday();
    }

    public List<ContentRanking> getActiveRankingsForDate(LocalDate date) {
        return contentRankingRepository.findActiveRankingsForDate(date);
    }

    public List<RankingItem> getActiveRankingItems(ContentRanking contentRanking) {
        return rankingItemRepository.findByContentRankingAndIsActiveTrueOrderByRankingAsc(contentRanking);
    }

    public List<RankingItem> getAllRankingItems(ContentRanking contentRanking) {
        return rankingItemRepository.findByContentRankingOrderByRankingAsc(contentRanking);
    }

    public List<ContentRanking> getAllRankings() {
        return contentRankingRepository.findAllByOrderByCreatedAtDesc();
    }

    public ContentRanking getRankingById(Long id) {
        return contentRankingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("랭킹을 찾을 수 없습니다: " + id));
    }

    @Transactional
    public ContentRanking saveRanking(ContentRanking contentRanking) {
        return contentRankingRepository.save(contentRanking);
    }

    @Transactional
    public void deleteRanking(Long id) {
        ContentRanking ranking = getRankingById(id);

        rankingItemRepository.deleteByContentRanking(ranking);

        contentRankingRepository.delete(ranking);
    }

    public RankingItem getRankingItemById(Long id) {
        return rankingItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("랭킹 항목을 찾을 수 없습니다: " + id));
    }

    @Transactional
    public RankingItem saveRankingItem(RankingItem rankingItem) {
        return rankingItemRepository.save(rankingItem);
    }

    @Transactional
    public void deleteRankingItem(Long id) {
        rankingItemRepository.deleteById(id);
    }

    @Transactional
    public ContentRanking toggleRankingStatus(Long id) {
        ContentRanking ranking = getRankingById(id);
        ranking.setIsActive(!ranking.getIsActive());
        return contentRankingRepository.save(ranking);
    }

    @Transactional
    public RankingItem toggleRankingItemStatus(Long id) {
        RankingItem item = getRankingItemById(id);
        item.setIsActive(!item.getIsActive());
        return rankingItemRepository.save(item);
    }
}