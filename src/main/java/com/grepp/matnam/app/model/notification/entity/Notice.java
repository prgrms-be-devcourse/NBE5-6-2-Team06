package com.grepp.matnam.app.model.notification.entity;

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
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type = NotificationType.NOTICE;

    private String message;

    private String link; // 공지 클릭 시 이동할 링크, 미구현 상태

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private boolean activated = true; // 기본 활성 상태

}
