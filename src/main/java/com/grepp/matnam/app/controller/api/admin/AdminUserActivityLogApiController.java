package com.grepp.matnam.app.controller.api.admin;

import com.grepp.matnam.app.model.log.UserActivityLogService;
import com.grepp.matnam.infra.response.ApiResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminUserActivityLogApiController {

    private final UserActivityLogService userActivityLogService;

    @GetMapping("/user/activity/monthly")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyUserActivityData() {
        return ResponseEntity.ok(ApiResponse.success(userActivityLogService.getMonthlyUserActivity()));
    }
}
