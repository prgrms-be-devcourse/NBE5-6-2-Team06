package com.grepp.matnam.app.model.notification.repository;

import com.grepp.matnam.app.model.notification.code.NotificationType;
import com.grepp.matnam.app.model.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    long countByUserIdAndIsReadFalseAndActivatedTrue(String userId);
    List<Notification> findByUserIdAndActivatedTrueOrderByCreatedAtDesc(String userId);
    List<Notification> findByUserIdAndActivatedTrueAndTypeOrderByCreatedAtDesc(String userId, NotificationType type);
    List<Notification> findByUserIdAndActivatedTrueAndIsReadFalseAndTypeOrderByCreatedAtDesc(String userId, NotificationType type);
    List<Notification> findByUserIdAndActivatedTrueAndIsReadFalseOrderByCreatedAtDesc(String userId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.userId = :userId AND n.id IN :notificationIds AND n.activated = true")
    void markAsReadByIds(@Param("userId") String userId, @Param("notificationIds") List<Long> notificationIds);

    @Modifying
    @Query("UPDATE Notification n SET n.activated = false WHERE n.userId = :userId AND n.id = :notificationId AND n.activated = true")
    void deactivateById(@Param("userId") String userId, @Param("notificationId") Long notificationId);

}
