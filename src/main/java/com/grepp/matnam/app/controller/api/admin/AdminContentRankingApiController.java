package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.model.content.dto.ContentRankingDTO;
import com.grepp.matnam.app.model.content.dto.RankingItemDTO;
import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.entity.RankingItem;
import com.grepp.matnam.app.model.content.service.ContentRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/content-rankings")
@RequiredArgsConstructor
public class AdminContentRankingApiController {

    private final ContentRankingService contentRankingService;

    @GetMapping
    public ResponseEntity<List<ContentRankingDTO>> getAllRankings() {
        List<ContentRankingDTO> rankings = contentRankingService.getAllRankings()
                .stream()
                .map(ContentRankingDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentRankingDTO> getRankingById(@PathVariable Long id) {
        try {
            ContentRanking ranking = contentRankingService.getRankingById(id);
            ContentRankingDTO dto = ContentRankingDTO.from(ranking);

            List<RankingItemDTO> items = contentRankingService.getAllRankingItems(ranking)
                    .stream()
                    .map(RankingItemDTO::from)
                    .collect(Collectors.toList());
            dto.setItems(items);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ContentRankingDTO> createRanking(@RequestBody ContentRankingDTO dto) {
        try {
            ContentRanking entity = dto.toEntity();
            ContentRanking saved = contentRankingService.saveRanking(entity);
            return ResponseEntity.ok(ContentRankingDTO.from(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentRankingDTO> updateRanking(
            @PathVariable Long id,
            @RequestBody ContentRankingDTO dto) {
        try {
            contentRankingService.getRankingById(id);

            dto.setId(id);

            ContentRanking entity = dto.toEntity();
            ContentRanking updated = contentRankingService.saveRanking(entity);

            return ResponseEntity.ok(ContentRankingDTO.from(updated));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRanking(@PathVariable Long id) {
        try {
            contentRankingService.deleteRanking(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ContentRankingDTO> toggleRankingStatus(@PathVariable Long id) {
        try {
            ContentRanking updated = contentRankingService.toggleRankingStatus(id);
            return ResponseEntity.ok(ContentRankingDTO.from(updated));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{rankingId}/items")
    public ResponseEntity<List<RankingItemDTO>> getRankingItems(@PathVariable Long rankingId) {
        try {
            ContentRanking ranking = contentRankingService.getRankingById(rankingId);

            List<RankingItemDTO> items = contentRankingService.getAllRankingItems(ranking)
                    .stream()
                    .map(RankingItemDTO::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{rankingId}/items")
    public ResponseEntity<RankingItemDTO> addRankingItem(
            @PathVariable Long rankingId,
            @RequestBody RankingItemDTO dto) {
        try {
            ContentRanking ranking = contentRankingService.getRankingById(rankingId);

            RankingItem entity = dto.toEntity(ranking);
            RankingItem saved = contentRankingService.saveRankingItem(entity);

            return ResponseEntity.ok(RankingItemDTO.from(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{rankingId}/items/{itemId}")
    public ResponseEntity<RankingItemDTO> updateRankingItem(
            @PathVariable Long rankingId,
            @PathVariable Long itemId,
            @RequestBody RankingItemDTO dto) {
        try {
            ContentRanking ranking = contentRankingService.getRankingById(rankingId);
            contentRankingService.getRankingItemById(itemId);

            dto.setId(itemId);

            RankingItem entity = dto.toEntity(ranking);
            RankingItem updated = contentRankingService.saveRankingItem(entity);

            return ResponseEntity.ok(RankingItemDTO.from(updated));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{rankingId}/items/{itemId}")
    public ResponseEntity<Void> deleteRankingItem(
            @PathVariable Long rankingId,
            @PathVariable Long itemId) {
        try {
            contentRankingService.getRankingById(rankingId);

            contentRankingService.deleteRankingItem(itemId);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{rankingId}/items/{itemId}/toggle")
    public ResponseEntity<RankingItemDTO> toggleRankingItemStatus(
            @PathVariable Long rankingId,
            @PathVariable Long itemId) {
        try {
            contentRankingService.getRankingById(rankingId);

            RankingItem updated = contentRankingService.toggleRankingItemStatus(itemId);

            return ResponseEntity.ok(RankingItemDTO.from(updated));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}