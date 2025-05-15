package com.grepp.matnam.app.controller.api.user;

import com.grepp.matnam.app.controller.api.user.payload.ReportRequest;
import com.grepp.matnam.app.model.user.ReportService;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Report API", description = "신고 API")
@RequiredArgsConstructor
public class ReportApiController {

    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "신고하기", description = "부적절한 모임 게시글이나 채팅을 신고합니다.")
    public ApiResponse createReport(@RequestBody ReportRequest reportRequest) {
        reportService.createReport(reportRequest);
        return ApiResponse.success(ResponseCode.OK);
    }
}
