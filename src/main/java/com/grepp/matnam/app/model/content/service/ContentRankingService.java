package com.grepp.matnam.app.model.content.service;

import com.grepp.matnam.app.model.content.dto.ContentRankingDTO;
import com.grepp.matnam.app.model.content.dto.ContentRankingWithItemsDto;
import com.grepp.matnam.app.model.content.dto.RankingItemDTO;
import com.grepp.matnam.app.model.content.dto.RankingItemSummaryDto;
import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.entity.RankingItem;
import com.grepp.matnam.app.model.content.repository.ContentRankingRepository;
import com.grepp.matnam.app.model.content.repository.RankingItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<ContentRanking> getTodayActiveRankings(Pageable pageable) {
        return contentRankingRepository.findActiveRankingsForToday(pageable);
    }

    public List<ContentRanking> getActiveRankingsForDate(LocalDate date) {
        return contentRankingRepository.findActiveRankingsForDate(date);
    }

    public Optional<ContentRanking> getCurrentActiveRanking() {
        return contentRankingRepository.findFirstActiveRankingForToday();
    }

    public Optional<ContentRankingWithItemsDto> getMainPageRanking() {
        return contentRankingRepository.findFirstActiveRankingForToday()
                .map(ranking -> {
                    List<RankingItemSummaryDto> items =
                            rankingItemRepository.findItemSummariesByRanking(ranking.getId());
                    return ContentRankingWithItemsDto.of(ranking, items);
                });
    }

    public List<ContentRanking> getAllRankings() {
        return contentRankingRepository.findAllByOrderByCreatedAtDesc();
    }

    public Page<ContentRanking> searchRankings(String title, Boolean isActive,
                                               String sortBy, Pageable pageable) {
        return contentRankingRepository.searchRankings(title, isActive, sortBy, pageable);
    }

    public List<ContentRanking> getRecentRankings(int limit) {
        return contentRankingRepository.findRecentRankings(limit);
    }

    public ContentRanking getRankingById(Long id) {
        return contentRankingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("랭킹을 찾을 수 없습니다: " + id));
    }

    @Transactional
    public ContentRanking saveRanking(ContentRanking contentRanking) {
        ContentRanking saved = contentRankingRepository.save(contentRanking);
        log.debug("랭킹 저장 완료: {}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteRanking(Long id) {
        ContentRanking ranking = getRankingById(id);

        long deletedItemsCount = rankingItemRepository.deleteAllItemsByRanking(id);
        log.debug("랭킹 {} 관련 아이템 {}개 삭제", id, deletedItemsCount);

        contentRankingRepository.delete(ranking);
        log.debug("랭킹 삭제 완료: {}", id);
    }

    public List<RankingItem> getActiveRankingItems(Long rankingId) {
        return rankingItemRepository.findActiveItemsByRanking(rankingId);
    }

    public List<RankingItem> getAllRankingItems(Long rankingId) {
        return rankingItemRepository.findAllItemsByRanking(rankingId);
    }

    public List<RankingItem> getActiveRankingItems(ContentRanking contentRanking) {
        return getActiveRankingItems(contentRanking.getId());
    }

    public List<RankingItem> getAllRankingItems(ContentRanking contentRanking) {
        return getAllRankingItems(contentRanking.getId());
    }

    public List<RankingItemSummaryDto> getRankingItemSummaries(Long rankingId) {
        return rankingItemRepository.findItemSummariesByRanking(rankingId);
    }

    public RankingItem getRankingItemById(Long id) {
        return rankingItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("랭킹 아이템을 찾을 수 없습니다: " + id));
    }

    @Transactional
    public RankingItem saveRankingItem(RankingItem rankingItem) {
        RankingItem saved = rankingItemRepository.save(rankingItem);
        log.debug("랭킹 아이템 저장 완료: {}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteRankingItem(Long id) {
        rankingItemRepository.deleteById(id);
        log.debug("랭킹 아이템 삭제 완료: {}", id);
    }

    @Transactional
    public int deactivateExpiredRankings() {
        long count = contentRankingRepository.deactivateExpiredRankings();
        log.info("만료된 랭킹 {}개를 비활성화했습니다.", count);
        return (int) count;
    }

    @Transactional
    public ContentRanking createRankingWithItems(ContentRankingDTO dto) {
        ContentRanking ranking = saveRanking(dto.toEntity());

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            List<RankingItem> items = dto.getItems().stream()
                    .map(itemDto -> itemDto.toEntity(ranking))
                    .toList();

            rankingItemRepository.saveAll(items);
            log.debug("랭킹과 아이템 생성 완료: 랭킹 ID {}, 아이템 {}개", ranking.getId(), items.size());
        }

        return ranking;
    }

    @Transactional
    public ContentRanking updateRankingWithItems(Long id, ContentRankingDTO dto) {
        ContentRanking existingRanking = getRankingById(id);

        rankingItemRepository.deleteAllItemsByRanking(id);

        existingRanking.setTitle(dto.getTitle());
        existingRanking.setSubtitle(dto.getSubtitle());
        existingRanking.setStartDate(dto.getStartDate());
        existingRanking.setEndDate(dto.getEndDate());
        existingRanking.setIsActive(dto.getIsActive());

        ContentRanking updatedRanking = contentRankingRepository.save(existingRanking);

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            List<RankingItem> newItems = dto.getItems().stream()
                    .map(itemDto -> itemDto.toEntity(updatedRanking))
                    .toList();

            rankingItemRepository.saveAll(newItems);
            log.debug("랭킹과 아이템 수정 완료: 랭킹 ID {}, 아이템 {}개", id, newItems.size());
        }

        return updatedRanking;
    }

    public ContentRankingDTO getRankingWithItems(Long id) {
        ContentRanking ranking = getRankingById(id);
        List<RankingItem> items = getAllRankingItems(id);

        ContentRankingDTO dto = ContentRankingDTO.from(ranking);
        List<RankingItemDTO> itemDTOs = items.stream()
                .map(RankingItemDTO::from)
                .toList();
        dto.setItems(itemDTOs);

        return dto;
    }
}