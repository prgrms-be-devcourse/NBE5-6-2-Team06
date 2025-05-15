package com.grepp.matnam.app.model.log;

import com.grepp.matnam.app.model.log.entity.UserActivityLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

    boolean existsByUserIdAndActivityDate(String userId, LocalDate activityDate);
    long countByActivityDate(LocalDate activityDate);

    @Query("SELECT " +
        "FUNCTION('DATE_FORMAT', u.activityDate, '%Y-%m') AS activityMonth, " +
        "COUNT(DISTINCT u.userId) AS uniqueUserCount " +
        "FROM UserActivityLog u " +
        "WHERE u.activityDate >= :startDate " +
        "GROUP BY FUNCTION('DATE_FORMAT', u.activityDate, '%Y-%m') " +
        "ORDER BY FUNCTION('DATE_FORMAT', u.activityDate, '%Y-%m')")
    List<Map<String, Object>> findMonthlyUserActivity(@Param("startDate") LocalDate startDate);
}
