package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.model.content.dto.ContentRankingDTO;
import com.grepp.matnam.app.model.content.dto.RankingItemDTO;
import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.entity.RankingItem;
import com.grepp.matnam.app.model.content.service.ContentRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/content-rankings")
@RequiredArgsConstructor
@Tag(name = "Admin Content Ranking API", description = "관리자가 사용자에게 보여줄 랭킹 콘텐츠를 관리하는 API")
public class AdminContentRankingApiController {

    private final ContentRankingService contentRankingService;

    @GetMapping
    @Operation(summary = "모든 랭킹 목록 조회", description = "시스템에 등록된 모든 랭킹 목록을 조회합니다.")
    public ResponseEntity<List<ContentRankingDTO>> getAllRankings() {
        List<ContentRankingDTO> rankings = contentRankingService.getAllRankings()
                .stream()
                .map(ContentRankingDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 랭킹 조회", description = "ID로 특정 랭킹과 그에 속한 항목들을 조회합니다.")
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
    @Operation(summary = "새로운 랭킹 생성", description = "새로운 랭킹을 생성합니다.")
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
    @Operation(summary = "랭킹 정보 수정", description = "기존 랭킹의 정보를 수정합니다.")
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
    @Operation(summary = "랭킹 삭제", description = "특정 ID의 랭킹을 삭제합니다.")
    public ResponseEntity<Void> deleteRanking(@PathVariable Long id) {
        try {
            contentRankingService.deleteRanking(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "랭킹 활성화 상태 토글", description = "랭킹의 활성화 상태를 변경합니다. 활성화된 랭킹만 사용자에게 표시됩니다.")
    public ResponseEntity<ContentRankingDTO> toggleRankingStatus(@PathVariable Long id) {
        try {
            ContentRanking updated = contentRankingService.toggleRankingStatus(id);
            return ResponseEntity.ok(ContentRankingDTO.from(updated));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{rankingId}/items")
    @Operation(summary = "랭킹 항목 목록 조회", description = "특정 랭킹에 속한 모든 항목을 조회합니다.")
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
    @Operation(summary = "랭킹 항목 추가", description = "특정 랭킹에 새로운 항목을 추가합니다.")
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
    @Operation(summary = "랭킹 항목 수정", description = "특정 랭킹의 항목 정보를 수정합니다.")
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
    @Operation(summary = "랭킹 항목 삭제", description = "특정 랭킹의 항목을 삭제합니다.")
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
    @Operation(summary = "랭킹 항목 활성화 상태 토글", description = "랭킹 항목의 활성화 상태를 변경합니다. 활성화된 항목만 사용자에게 표시됩니다.")
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