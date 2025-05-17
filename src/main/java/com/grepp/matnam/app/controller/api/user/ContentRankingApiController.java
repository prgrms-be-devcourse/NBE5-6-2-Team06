
package com.grepp.matnam.app.controller.api.user;

import com.grepp.matnam.app.model.content.dto.ContentRankingDTO;
import com.grepp.matnam.app.model.content.dto.RankingItemDTO;
import com.grepp.matnam.app.model.content.entity.ContentRanking;
import com.grepp.matnam.app.model.content.service.ContentRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/content-rankings")
@RequiredArgsConstructor
public class ContentRankingApiController {

    private final ContentRankingService contentRankingService;

    @GetMapping
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