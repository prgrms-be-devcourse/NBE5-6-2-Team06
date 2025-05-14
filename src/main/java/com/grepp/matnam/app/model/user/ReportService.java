package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.dto.ReportDto;
import com.grepp.matnam.app.model.user.entity.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Page<ReportDto> findByFilter(Boolean status, String keyword, Pageable pageable) {
        if (status != null && StringUtils.hasText(keyword)) {
            return reportRepository.findByStatusAndKeywordContaining(status, keyword, pageable);
        } else if (status != null) {
            return reportRepository.findByStatus(status, pageable);
        } else if (StringUtils.hasText(keyword)) {
            return reportRepository.findByKeywordContaining(keyword, pageable);
        } else {
            return reportRepository.findAllReports(pageable);
        }
    }

    public void unActivatedById(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));
        report.unActivated();
    }
}
