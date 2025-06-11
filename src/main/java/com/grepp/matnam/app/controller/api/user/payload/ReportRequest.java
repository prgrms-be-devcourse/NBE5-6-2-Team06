package com.grepp.matnam.app.controller.api.user.payload;

import com.grepp.matnam.app.model.user.code.ReportType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    @NotBlank(message = "신고자 ID는 필수입니다.")
    private String userId;
    @NotBlank(message = "신고 대상자 ID는 필수입니다.")
    private String reportedId;
    @NotBlank(message = "신고 사유는 필수입니다.")
    private String reason;
    private ReportType reportType;
    private Long teamId;
    private Long chatId;

    @AssertTrue(message = "모임 ID 또는 채팅 ID 중 하나는 필수입니다.")
    public boolean isValidReportTarget() {
        return (teamId != null && chatId == null) || (teamId == null && chatId != null);
    }
}