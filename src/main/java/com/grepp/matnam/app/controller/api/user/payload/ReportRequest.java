package com.grepp.matnam.app.controller.api.user.payload;

import com.grepp.matnam.app.model.user.code.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    private String userId;
    private String reportedId;
    private String reason;
    private ReportType reportType;
    private Long teamId;
    private Long chatId;
}