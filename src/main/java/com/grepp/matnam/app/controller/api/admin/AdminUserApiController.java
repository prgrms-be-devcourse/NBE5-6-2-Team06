package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.admin.payload.AgeDistributionResponse;
import com.grepp.matnam.app.controller.api.admin.payload.ReportChatResponse;
import com.grepp.matnam.app.controller.api.admin.payload.ReportTeamResponse;
import com.grepp.matnam.app.controller.api.admin.payload.UserStatusRequest;
import com.grepp.matnam.app.model.user.PreferenceService;
import com.grepp.matnam.app.model.user.ReportService;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.code.Gender;
import com.grepp.matnam.infra.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Admin User API", description = "관리자 - 사용자 관리 API")
public class AdminUserApiController {
    private final UserService userService;
    private final ReportService reportService;
    private final PreferenceService preferenceService;

    @PatchMapping("/list/{userId}")
    @Operation(summary = "사용자 상태 수정", description = "특정 사용자의 상태(정상, 정지, 영구정지)를 수정합니다.")
    public ResponseEntity<?> updateUserStatus(@PathVariable String userId,
        @RequestBody UserStatusRequest request) {

        userService.updateUserStatus(userId, request);
        return ResponseEntity.ok("사용자 상태가 수정되었습니다.");
    }

    @DeleteMapping("/list/{userId}")
    @Operation(summary = "사용자 비활성화", description = "특정 사용자를 비활성화합니다.")
    public ResponseEntity<?> unActivatedUser(@PathVariable String userId) {
        userService.unActivatedById(userId);
        return ResponseEntity.ok("사용자가 비활성화되었습니다.");
    }

    @PatchMapping("/report/{reportId}")
    @Operation(summary = "신고 처리", description = "특정 신고를 처리합니다.")
    public ResponseEntity<?> unActivatedReport(@PathVariable Long reportId) {
        reportService.unActivatedById(reportId);
        return ResponseEntity.ok("신고 처리가 완료되었습니다.");
    }

    @GetMapping("/report/team/{teamId}")
    @Operation(summary = "모임 게시글 신고 내역 조회", description = "모임 게시글을 신고한 내역을 조회합니다.")
    public ResponseEntity<ApiResponse<ReportTeamResponse>> getReportTeam(@PathVariable Long teamId) {
        ReportTeamResponse reportTeamResponse = reportService.getTeamByTeamId(teamId);
        return ResponseEntity.ok(ApiResponse.success(reportTeamResponse));
    }

    @GetMapping("/report/chat/{chatId}")
    @Operation(summary = "채팅 신고 내역 조회", description = "채팅을 신고한 내역을 조회합니다.")
    public ResponseEntity<ApiResponse<ReportChatResponse>> getReportChat(@PathVariable Long chatId) {
        ReportChatResponse reportChatResponse = reportService.getChatByChatId(chatId);
        return ResponseEntity.ok(ApiResponse.success(reportChatResponse));
    }

    @GetMapping("/statistics/age-distribution")
    @Operation(summary = "연령대별 사용자 수 조회", description = "연령대별 사용자 수를 조회합니다.")
    public ResponseEntity<ApiResponse<List<AgeDistributionResponse>>> getAgeDistribution() {
        List<AgeDistributionResponse> ageDistribution = userService.getAgeDistribution();
        return ResponseEntity.ok(ApiResponse.success(ageDistribution));
    }

    @GetMapping("/statistics/gender-distribution")
    @Operation(summary = "성별 사용자 수 조회", description = "성별 사용자 수를 조회합니다.")
    public ResponseEntity<ApiResponse<Map<Gender, Long>>> getGenderDistribution() {
        Map<Gender, Long> genderCounts = userService.getGenderDistribution();
        return ResponseEntity.ok(ApiResponse.success(genderCounts));
    }

    @GetMapping("/statistics/preference-counts")
    @Operation(summary = "사용자 취향 선호도 조회", description = "사용자 취향 선호도를 조회합니다.")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getPreferenceCounts() {
        Map<String, Long> counts = preferenceService.getPreferenceCounts();
        return ResponseEntity.ok(ApiResponse.success(counts));
    }
}
