package com.grepp.matnam.app.model.notification.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grepp.matnam.app.model.notification.code.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message; // 알림 내용

    private String link; // 알림 클릭 시 이동할 링크 (선택 사항)

    @JsonProperty("isRead") // JSON 직렬화 과정에서 read 로 변환되는 현상 방지
    private boolean isRead; // 읽음 여부

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private boolean activated; // 비활성화
}