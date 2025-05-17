package com.grepp.matnam.app.model.content.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ranking_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_ranking_id", nullable = false)
    private ContentRanking contentRanking;

    @Column(nullable = false)
    private Integer ranking;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}