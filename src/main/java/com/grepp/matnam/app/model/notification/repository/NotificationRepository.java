package com.grepp.matnam.app.model.notification.repository;

import com.grepp.matnam.app.model.notification.code.NotificationType;
import com.grepp.matnam.app.model.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>,
    NotificationRepositoryCustom {

    long countByUserIdAndIsReadFalseAndActivatedTrue(String userId);

    List<Notification> findByUserIdAndActivatedTrueOrderByCreatedAtDesc(String userId);

    List<Notification> findByUserIdAndActivatedTrueAndTypeOrderByCreatedAtDesc(String userId,
        NotificationType type);

    List<Notification> findByUserIdAndActivatedTrueAndIsReadFalseAndTypeOrderByCreatedAtDesc(
        String userId, NotificationType type);

    List<Notification> findByUserIdAndActivatedTrueAndIsReadFalseOrderByCreatedAtDesc(
        String userId);
}
