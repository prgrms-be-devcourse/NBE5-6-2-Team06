package com.grepp.matnam.app.model.notification.repository;

import com.grepp.matnam.app.model.notification.entity.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QNotification notification = QNotification.notification;

    @Override
    public long markAsReadByIds(String userId, List<Long> notificationIds) {
        return queryFactory
            .update(notification)
            .set(notification.isRead, true)
            .where(
                notification.userId.eq(userId),
                notification.id.in(notificationIds),
                notification.activated.isTrue()
            )
            .execute();
    }

    @Override
    public long deactivateById(String userId, Long notificationId) {
        return queryFactory
            .update(notification)
            .set(notification.activated, false)
            .where(
                notification.userId.eq(userId),
                notification.id.eq(notificationId),
                notification.activated.isTrue()
            )
            .execute();
    }
}
