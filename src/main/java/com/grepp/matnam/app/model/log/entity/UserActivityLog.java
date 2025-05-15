package com.grepp.matnam.app.model.log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private LocalDate activityDate;

    private LocalDateTime timestamp;

    public UserActivityLog(String userId) {
        this.userId = userId;
        this.activityDate = LocalDate.now();
        this.timestamp = LocalDateTime.now();
    }

    protected UserActivityLog() {}
}
