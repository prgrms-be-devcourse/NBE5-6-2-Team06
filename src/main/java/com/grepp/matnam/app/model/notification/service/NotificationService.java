package com.grepp.matnam.app.model.notification.service;

import com.grepp.matnam.app.model.notification.code.NotificationType;
import com.grepp.matnam.app.model.notification.entity.Notice;
import com.grepp.matnam.app.model.notification.entity.Notification;
import com.grepp.matnam.app.model.notification.repository.NoticeRepository;
import com.grepp.matnam.app.model.notification.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NoticeRepository noticeRepository;

    public long getUnreadNotificationCount(String userId) {
        return notificationRepository.countByUserIdAndIsReadFalseAndActivatedTrue(userId);
    }

    public List<Notification> getAllNotifications(String userId) {
        return notificationRepository.findByUserIdAndActivatedTrueOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndActivatedTrueAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getSystemNotifications(String userId) {
        return notificationRepository.findByUserIdAndActivatedTrueAndTypeOrderByCreatedAtDesc(userId, NotificationType.NOTICE);
    }

    // SSE 연결 시 초기 데이터 전송을 위해 List로 반환
    public List<Notification> getUnreadNotificationsForSSE(String userId) {
        return notificationRepository.findByUserIdAndActivatedTrueAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void markAsRead(String userId, List<Long> notificationIds) {
        notificationRepository.markAsReadByIds(userId, notificationIds);
    }

    @Transactional
    public Notification createNotification(String userId, NotificationType type, String message, String link) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setLink(link);
        notification.setRead(false);
        notification.setActivated(true);
        return notificationRepository.save(notification);
    }

    @Transactional
    public void deactivateNotification(String userId, Long notificationId) {
        notificationRepository.deactivateById(userId, notificationId);
    }

    @Transactional
    public void saveNotice(String message, String link) {
        Notice notice = new Notice();
        notice.setMessage(message);
        notice.setLink(link);
        noticeRepository.save(notice);
    }

    public Page<Notice> findByFilter(String keyword, Pageable pageable) {
        if (StringUtils.hasText(keyword)) {
            return noticeRepository.findByKeywordContaining(keyword, pageable);
        } else {
            return noticeRepository.findAllNotices(pageable);
        }
    }

    @Transactional
    public void unActivatedById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        notice.setActivated(false);
    }

    public Notice getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
    }
}
