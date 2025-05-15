package com.grepp.matnam.app.model.log;

import com.grepp.matnam.app.controller.web.admin.payload.UserActivityLogResponse;
import com.grepp.matnam.app.model.log.entity.UserActivityLog;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserActivityLogService {

    private final UserActivityLogRepository userActivityLogRepository;

    public void logIfFirstToday(String userId) {
        if (!userActivityLogRepository.existsByUserIdAndActivityDate(userId, LocalDate.now())) {
            userActivityLogRepository.save(new UserActivityLog(userId));
        }
    }

    public UserActivityLogResponse getTodayLogStats() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        long todayCount = userActivityLogRepository.countByActivityDate(today);
        long yesterdayCount = userActivityLogRepository.countByActivityDate(yesterday);

        String growthRate = calculateGrowthRate(todayCount, yesterdayCount);
        return new UserActivityLogResponse(todayCount, growthRate);
    }

    private String calculateGrowthRate(long today, long yesterday) {
        if (yesterday == 0) {
            if (today == 0) return "+0%";
            return "+100%"; // 또는 "N/A"
        }
        long diff = today - yesterday;
        double percent = ((double) diff / yesterday) * 100;
        String sign = percent >= 0 ? "+" : "";
        return String.format("%s%.0f%%", sign, percent);
    }
}
