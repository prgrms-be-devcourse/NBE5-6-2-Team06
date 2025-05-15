package com.grepp.matnam.app.model.log;

import com.grepp.matnam.app.model.log.entity.UserActivityLog;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

    boolean existsByUserIdAndActivityDate(String userId, LocalDate activityDate);
    long countByActivityDate(LocalDate activityDate);
}
