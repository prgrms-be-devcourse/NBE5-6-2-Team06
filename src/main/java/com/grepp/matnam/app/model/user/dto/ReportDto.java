package com.grepp.matnam.app.model.user.dto;

import com.grepp.matnam.app.model.user.code.ReportType;
import com.grepp.matnam.app.model.user.code.Status;
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
    private String nickname;
    private String email;
    private Status status;
    private Integer suspendDuration;
    private String dueReason;
    private String reason;
    private LocalDateTime createdAt;
    private Boolean activated;
    private ReportType reportType;
    private Long chatId;
    private Long teamId;
}
