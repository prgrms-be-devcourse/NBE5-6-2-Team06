
package com.grepp.matnam.app.controller.api.user;

import com.grepp.matnam.app.model.content.dto.ContentRankingDTO;
import com.grepp.matnam.app.model.content.dto.RankingItemDTO;
import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.service.ContentRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "User Content Ranking API", description = "사용자 관리 랭킹 콘텐츠 API")
@RequestMapping("/api/content-rankings")
@RequiredArgsConstructor
public class ContentRankingApiController {

    private final ContentRankingService contentRankingService;

    @GetMapping
    @Operation(summary = "랭킹 가져오기", description = "현재 활성화된 랭킹 목록을 가져옵니다.")
    public ResponseEntity<List<ContentRankingDTO>> getCurrentRankings() {
        List<ContentRanking> rankings = contentRankingService.getTodayActiveRankings();
        if (rankings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ContentRankingDTO> result = rankings.stream()
                .map(ContentRankingDTO::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "특정 날짜의 활성화된 랭킹 목록 조회", description = "지정된 날짜에 활성화된 모든 랭킹 콘텐츠 목록을 조회합니다.")
    public ResponseEntity<List<ContentRankingDTO>> getRankingsByDate(@PathVariable String date) {
        try {
            LocalDate targetDate = LocalDate.parse(date);
            List<ContentRanking> rankings = contentRankingService.getActiveRankingsForDate(targetDate);

            if (rankings.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<ContentRankingDTO> result = rankings.stream()
                    .map(ContentRankingDTO::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 랭킹의 상세 정보 조회", description = "지정된 ID의 랭킹 콘텐츠 상세 정보와 랭킹 항목 목록을 조회합니다.")
    public ResponseEntity<ContentRankingDTO> getRankingDetail(@PathVariable Long id) {
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

            List<RankingItemDTO> items = contentRankingService.getActiveRankingItems(ranking)
                    .stream()
                    .map(RankingItemDTO::from)
                    .collect(Collectors.toList());
            dto.setItems(items);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}