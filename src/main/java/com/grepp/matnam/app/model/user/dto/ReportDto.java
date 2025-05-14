package com.grepp.matnam.app.model.user.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private Long reportId;
    private String userId;
    private String reportedId;
    private String reason;
    private LocalDateTime createdAt;
    private Boolean activated;
}
