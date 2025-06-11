package com.grepp.matnam.app.model.content.repository;

import com.grepp.matnam.app.model.content.entity.ContentRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRankingRepository extends JpaRepository<ContentRanking, Long>, ContentRankingRepositoryCustom {

    @Query("SELECT cr FROM ContentRanking cr WHERE cr.isActive = true " +
            "AND cr.startDate <= CURRENT_DATE " +
            "AND (cr.endDate IS NULL OR cr.endDate >= CURRENT_DATE)")
    List<ContentRanking> findActiveRankingsForToday();

    @Query("SELECT cr FROM ContentRanking cr WHERE cr.isActive = true " +
            "AND cr.startDate <= :date " +
            "AND (cr.endDate IS NULL OR cr.endDate >= :date)")
    List<ContentRanking> findActiveRankingsForDate(LocalDate date);

    @Query("SELECT cr FROM ContentRanking cr WHERE cr.isActive = true " +
            "AND cr.startDate <= CURRENT_DATE " +
            "AND (cr.endDate IS NULL OR cr.endDate >= CURRENT_DATE)")
    Optional<ContentRanking> findFirstActiveRankingForToday();

    List<ContentRanking> findAllByOrderByCreatedAtDesc();
}