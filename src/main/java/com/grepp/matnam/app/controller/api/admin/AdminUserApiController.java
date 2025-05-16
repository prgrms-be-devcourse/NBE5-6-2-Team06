package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.controller.api.admin.payload.ReportChatResponse;
import com.grepp.matnam.app.controller.api.admin.payload.ReportTeamResponse;
import com.grepp.matnam.app.controller.api.admin.payload.UserStatusRequest;
import com.grepp.matnam.app.model.user.ReportService;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.infra.response.ApiResponse;
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
public class AdminUserApiController {
    private final UserService userService;
    private final ReportService reportService;

    @PatchMapping("/list/{userId}")
    public ResponseEntity<?> updateUserStatus(@PathVariable String userId,
        @RequestBody UserStatusRequest request) {

        userService.updateUserStatus(userId, request);
        return ResponseEntity.ok("사용자 상태가 수정되었습니다.");
    }

    @DeleteMapping("/list/{userId}")
    public ResponseEntity<?> unActivatedUser(@PathVariable String userId) {
        userService.unActivatedById(userId);
        return ResponseEntity.ok("사용자가 비활성화되었습니다.");
    }

    @DeleteMapping("/report/{reportId}")
    public ResponseEntity<?> unActivatedReport(@PathVariable Long reportId) {
        reportService.unActivatedById(reportId);
        return ResponseEntity.ok("신고 처리가 완료되었습니다.");
    }

    @GetMapping("/report/team/{teamId}")
    public ResponseEntity<ApiResponse<ReportTeamResponse>> getTeam(@PathVariable Long teamId) {
        ReportTeamResponse reportTeamResponse = reportService.getTeamByTeamId(teamId);
        return ResponseEntity.ok(ApiResponse.success(reportTeamResponse));
    }

    @GetMapping("/report/chat/{chatId}")
    public ResponseEntity<ApiResponse<ReportChatResponse>> getChat(@PathVariable Long chatId) {
        ReportChatResponse reportChatResponse = reportService.getChatByChatId(chatId);
        return ResponseEntity.ok(ApiResponse.success(reportChatResponse));
    }

}
