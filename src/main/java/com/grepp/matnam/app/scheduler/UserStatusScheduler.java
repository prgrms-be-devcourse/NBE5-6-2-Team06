package com.grepp.matnam.app.scheduler;

import com.grepp.matnam.app.model.user.UserRepository;
import com.grepp.matnam.app.model.user.code.Status;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserStatusScheduler {

    private final UserRepository userRepository;

    // 매일 자정에 실행 (초, 분, 시, 일, 월, 요일)
    @Scheduled(cron = "0 0 0 * * *")
    public void activateUsersWithExpiredSuspension() {
        LocalDate today = LocalDate.now();

        List<User> usersToActivate = userRepository.findAllByStatusAndDueDateBefore(Status.SUSPENDED, today);

        for (User user : usersToActivate) {
            user.setStatus(Status.ACTIVE);
            user.setDueDate(null);
            user.setDueReason(null);
            user.setSuspendDuration(null);
        }

        userRepository.saveAll(usersToActivate);
    }
}
