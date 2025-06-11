package com.grepp.matnam.app.controller.api.user;

import com.grepp.matnam.app.model.content.dto.ContentRankingDTO;
import com.grepp.matnam.app.model.content.dto.ContentRankingWithItemsDto;
import com.grepp.matnam.app.model.content.dto.RankingItemDTO;
import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.service.ContentRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "User Content Ranking API", description = "사용자 관리 랭킹 콘텐츠 API (QueryDSL)")
@RequestMapping("/api/content-rankings")
@RequiredArgsConstructor
public class ContentRankingApiController {

    private final ContentRankingService contentRankingService;

    @GetMapping
    @Operation(summary = "현재 활성화된 랭킹 조회", description = "오늘 날짜 기준으로 활성화된 랭킹 목록을 페이징으로 조회합니다.")
    public ResponseEntity<Page<ContentRankingDTO>> getCurrentRankings(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<ContentRanking> rankings = contentRankingService.getTodayActiveRankings(pageable);

        Page<ContentRankingDTO> result = rankings.map(ContentRankingDTO::from);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/main")
    @Operation(summary = "메인 페이지용 랭킹 조회", description = "메인 페이지에 표시할 랭킹과 아이템 정보를 함께 조회합니다. (QueryDSL 성능 최적화)")
    public ResponseEntity<ContentRankingDTO> getMainPageRanking() {
        Optional<ContentRankingWithItemsDto> rankingWithItems =
                contentRankingService.getMainPageRanking();

        if (rankingWithItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ContentRankingWithItemsDto data = rankingWithItems.get();
        ContentRankingDTO result = ContentRankingDTO.from(data.getRanking());

        List<RankingItemDTO> items = data.getItems().stream()
                .map(item -> RankingItemDTO.builder()
                        .id(item.getId())
                        .ranking(item.getRanking())
                        .itemName(item.getItemName())
                        .isActive(item.getIsActive())
                        .build())
                .toList();

        result.setItems(items);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 랭킹의 상세 정보 조회", description = "지정된 ID의 랭킹 콘텐츠 상세 정보와 랭킹 항목 목록을 조회합니다.")
    public ResponseEntity<ContentRankingDTO> getRankingDetail(
            @Parameter(description = "랭킹 ID", example = "1")
            @PathVariable Long id) {
        try {
            ContentRanking ranking = contentRankingService.getRankingById(id);

            if (!ranking.getIsActive()) {
                return ResponseEntity.notFound().build();
            }

            LocalDate today = LocalDate.now();
            if (ranking.getStartDate().isAfter(today) ||
                    (ranking.getEndDate() != null && ranking.getEndDate().isBefore(today))) {
                return ResponseEntity.notFound().build();
            }

            ContentRankingDTO dto = ContentRankingDTO.from(ranking);

            List<RankingItemDTO> items = contentRankingService.getRankingItemSummaries(ranking.getId())
                    .stream()
                    .map(item -> RankingItemDTO.builder()
                            .id(item.getId())
                            .ranking(item.getRanking())
                            .itemName(item.getItemName())
                            .isActive(item.getIsActive())
                            .build())
                    .toList();

            dto.setItems(items);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}