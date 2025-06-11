package com.grepp.matnam.app.model.content.repository;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContentRankingRepositoryCustom {

    List<ContentRanking> findActiveRankingsForToday();

    Page<ContentRanking> findActiveRankingsForToday(Pageable pageable);

    List<ContentRanking> findActiveRankingsForDate(LocalDate date);

    Optional<ContentRanking> findFirstActiveRankingForToday();

    Page<ContentRanking> searchRankings(String title, Boolean isActive,
                                        String sortBy, Pageable pageable);

    long deactivateExpiredRankings();

    List<ContentRanking> findRecentRankings(int limit);

    List<ContentRanking> findAllByOrderByCreatedAtDesc();
}