package com.grepp.matnam.app.controller.api.user;

import com.grepp.matnam.app.controller.api.user.payload.*;
import com.grepp.matnam.app.model.log.UserActivityLogService;
import com.grepp.matnam.app.model.user.PreferenceService;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.auth.AuthenticationUtils;
import com.grepp.matnam.infra.auth.CookieUtils;
import com.grepp.matnam.infra.jwt.JwtTokenProvider;
import com.grepp.matnam.infra.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "사용자 관리 API")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final PreferenceService preferenceService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserActivityLogService userActivityLogService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<ApiResponse<JwtResponse>> signup(
            @Validated @RequestBody UserSignupRequest request,
            HttpServletResponse response) {

        User user = userService.signup(request.toEntity());

        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getRole().name());

        int maxAge = 86400;
        CookieUtils.addJwtCookie(response, token, maxAge);
        CookieUtils.addUserNicknameCookie(response, user.getNickname(), maxAge);

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getUserId())
                .role(user.getRole().name())
                .expiration(maxAge)
                .build();

        return ResponseEntity.ok(ApiResponse.success(jwtResponse));
    }

    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "사용자 로그인 후 JWT 토큰을 발급합니다.")
    public ResponseEntity<ApiResponse<JwtResponse>> signin(
            @Validated @RequestBody UserSigninRequest request,
            HttpServletResponse response) {

        User user = userService.signin(request.getUserId(), request.getPassword());

        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getRole().name());

        int maxAge = 86400;
        CookieUtils.addJwtCookie(response, token, maxAge);
        CookieUtils.addUserNicknameCookie(response, user.getNickname(), maxAge);

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getUserId())
                .role(user.getRole().name())
                .expiration(maxAge)
                .build();

        userActivityLogService.logIfFirstToday(user.getUserId());

        return ResponseEntity.ok(ApiResponse.success(jwtResponse));
    }

    @PostMapping("/preference")
    @Operation(summary = "취향 설정", description = "사용자의 취향을 설정합니다.")
    public ResponseEntity<ApiResponse<PreferenceRequest>> setPreference(@Validated @RequestBody PreferenceRequest request) {
        String currentUserId = AuthenticationUtils.getCurrentUserId();

        preferenceService.savePreference(currentUserId, request);

        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @PutMapping("/preference")
    @Operation(summary = "취향 변경", description = "사용자의 취향을 변경합니다.")
    public ResponseEntity<ApiResponse<PreferenceRequest>> updatePreference(@Validated @RequestBody PreferenceRequest request) {
        String currentUserId = AuthenticationUtils.getCurrentUserId();

        preferenceService.updatePreference(currentUserId, request);

        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "사용자 계정을 비활성화(탈퇴) 처리합니다.")
    public ResponseEntity<ApiResponse<Void>> deactivateAccount(@Validated @RequestBody UserDeactivateRequest request) {
        String currentUserId = AuthenticationUtils.getCurrentUserId();

        userService.deactivateAccount(currentUserId, request.getPassword());

        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @PutMapping("/password")
    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Validated @RequestBody PasswordChangeRequest request) {
        String currentUserId = AuthenticationUtils.getCurrentUserId();

        userService.changePassword(currentUserId, request.getCurrentPassword(), request.getNewPassword());

        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @GetMapping("{userId}/temperature")
    @Operation(summary = "사용자 매너온도 조회", description = "특정 사용자의 매너 온도를 조회합니다.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserTemperature(@PathVariable String userId) {
        User user = userService.getUserById(userId);

        Map<String, Object> temperature = new HashMap<>();

        temperature.put("userId", user.getUserId());
        temperature.put("temperature", user.getTemperature());

        return ResponseEntity.ok(ApiResponse.success(temperature));
    }
}