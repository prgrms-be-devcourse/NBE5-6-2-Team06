package com.grepp.matnam.app.model.log;

import com.grepp.matnam.app.controller.web.admin.payload.UserActivityLogResponse;
import com.grepp.matnam.app.model.log.entity.UserActivityLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    public List<Map<String, Object>> getMonthlyUserActivity() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6).withDayOfMonth(1);
        return userActivityLogRepository.findMonthlyUserActivity(sixMonthsAgo);
    }

    public List<Map<String, Object>> getWeekUserActivity() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return userActivityLogRepository.findWeekUserActivity(sevenDaysAgo);
    }
}
