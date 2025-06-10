package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.admin.payload.StatLongResponse;
import com.grepp.matnam.app.model.log.UserActivityLogService;
import com.grepp.matnam.infra.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user-activity")
@RequiredArgsConstructor
@Tag(name = "Admin User Activity Log API", description = "관리자 - 사용자 활동 로그 API")
public class AdminUserActivityLogApiController {

    private final UserActivityLogService userActivityLogService;

    @GetMapping("/statistics/monthly")
    @Operation(summary = "최근 6개월 이용 회원 수 조회", description = "최근 6개월간의 이용 회원 수를 조회합니다.")
    public ResponseEntity<ApiResponse<List<StatLongResponse>>> getMonthlyUserActivity() {
        return ResponseEntity.ok(ApiResponse.success(userActivityLogService.getMonthlyUserActivity()));
    }

    @GetMapping("/statistics/week")
    @Operation(summary = "최근 7일 이용 회원 수 조회", description = "최근 7일간의 이용 회원 수를 조회합니다.")
    public ResponseEntity<ApiResponse<List<StatLongResponse>>> getWeekUserActivity() {
        return ResponseEntity.ok(ApiResponse.success(userActivityLogService.getWeekUserActivity()));
    }
}
