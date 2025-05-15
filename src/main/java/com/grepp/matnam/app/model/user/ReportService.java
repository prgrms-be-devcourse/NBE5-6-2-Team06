package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.controller.api.admin.payload.ReportChatResponse;
import com.grepp.matnam.app.controller.api.admin.payload.ReportTeamResponse;
import com.grepp.matnam.app.controller.api.user.payload.ReportRequest;
import com.grepp.matnam.app.model.chat.repository.ChatRepository;
import com.grepp.matnam.app.model.team.TeamRepository;
import com.grepp.matnam.app.model.user.dto.ReportDto;
import com.grepp.matnam.app.model.user.entity.Report;
import com.grepp.matnam.app.model.user.entity.User;
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
    private final UserService userService;
    private final TeamRepository teamRepository;
    private final ChatRepository chatRepository;

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

    public void createReport(ReportRequest reportRequest) {
        User user = userService.getUserById(reportRequest.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }

        User reportedUser = userService.getUserById(reportRequest.getReportedId());
        if (reportedUser == null) {
            throw new IllegalArgumentException("유효하지 않은 신고 대상자입니다.");
        }

        Report report = new Report();
        report.setUser(user);
        report.setReportedUser(reportedUser);
        report.setReason(reportRequest.getReason());
        report.setReportType(reportRequest.getReportType());
        report.setChatId(reportRequest.getChatId());
        report.setTeamId(reportRequest.getTeamId());

        reportRepository.save(report);
    }

    public ReportTeamResponse getTeamByTeamId(Long teamId) {
        return new ReportTeamResponse(teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("모임을 찾을 수 없습니다.")));
    }

    public ReportChatResponse getChatByChatId(Long chatId) {
        return new ReportChatResponse(chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("채팅을 찾을 수 없습니다.")));
    }
}
