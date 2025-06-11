package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.model.content.dto.ContentRankingDTO;
import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.service.ContentRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/content-rankings")
@RequiredArgsConstructor
@Tag(name = "Admin Content Ranking API", description = "관리자용 랭킹 콘텐츠 관리 API")
public class AdminContentRankingApiController {

    private final ContentRankingService contentRankingService;

    @GetMapping
    @Operation(summary = "랭킹 검색 및 목록 조회", description = "동적 검색 조건을 사용하여 랭킹 목록을 조회합니다.")
    public ResponseEntity<Page<ContentRankingDTO>> searchRankings(
            @Parameter(description = "검색할 제목") @RequestParam(required = false) String title,
            @Parameter(description = "활성화 상태") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "정렬 기준 (newest, oldest, title, title_desc)") @RequestParam(defaultValue = "newest") String sortBy,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<ContentRanking> rankings = contentRankingService.searchRankings(
                title, isActive, sortBy, pageable);
        Page<ContentRankingDTO> result = rankings.map(ContentRankingDTO::from);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 랭킹 조회", description = "ID로 특정 랭킹과 그에 속한 항목들을 조회합니다.")
    public ResponseEntity<ContentRankingDTO> getRankingById(@PathVariable Long id) {
        try {
            ContentRankingDTO dto = contentRankingService.getRankingWithItems(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "새로운 랭킹 생성", description = "새로운 랭킹과 아이템들을 함께 생성합니다.")
    public ResponseEntity<ContentRankingDTO> createRanking(@Valid @RequestBody ContentRankingDTO dto) {
        try {
            ContentRanking saved = contentRankingService.createRankingWithItems(dto);
            return ResponseEntity.ok(ContentRankingDTO.from(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "랭킹 정보 수정", description = "기존 랭킹과 아이템들을 함께 수정합니다.")
    public ResponseEntity<ContentRankingDTO> updateRanking(
            @PathVariable Long id,
            @Valid @RequestBody ContentRankingDTO dto) {
        try {
            ContentRanking updated = contentRankingService.updateRankingWithItems(id, dto);
            return ResponseEntity.ok(ContentRankingDTO.from(updated));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "랭킹 삭제", description = "특정 ID의 랭킹과 관련된 모든 아이템을 삭제합니다.")
    public ResponseEntity<Void> deleteRanking(@PathVariable Long id) {
        try {
            contentRankingService.deleteRanking(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/items/count")
    @Operation(summary = "랭킹 아이템 개수 조회", description = "특정 랭킹의 아이템 개수를 조회합니다.")
    public ResponseEntity<Map<String, Integer>> getRankingItemCount(@PathVariable Long id) {
        try {
            int count = contentRankingService.getAllRankingItems(id).size();
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("count", 0));
        }
    }
}