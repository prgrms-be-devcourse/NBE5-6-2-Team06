package com.grepp.matnam.app.scheduler;

import com.grepp.matnam.app.model.user.UserRepository;
import com.grepp.matnam.app.model.user.code.Status;
import com.grepp.matnam.app.model.user.entity.User;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class UserStatusSchedulerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusScheduler userStatusScheduler;

    @Test
    @Transactional
    @Rollback
    public void testActivateUsersWithExpiredSuspension() {
        // given: 어제 날짜로 정지된 유저 저장
        User suspendedUser = new User();
        suspendedUser.setUserId("SchedulerTest");
        suspendedUser.setStatus(Status.SUSPENDED);
        suspendedUser.setDueDate(LocalDate.now().minusDays(1));
        suspendedUser.setDueReason("테스트 정지");
        userRepository.save(suspendedUser);

        // when: 스케줄러 실행
        userStatusScheduler.activateUsersWithExpiredSuspension();

        // then: 상태가 ACTIVE로 변경되었는지 확인
        User updatedUser = userRepository.findById("SchedulerTest").orElseThrow();
        Assertions.assertEquals(Status.ACTIVE, updatedUser.getStatus());
        Assertions.assertNull(updatedUser.getDueDate());
        Assertions.assertNull(updatedUser.getDueReason());
    }
}
