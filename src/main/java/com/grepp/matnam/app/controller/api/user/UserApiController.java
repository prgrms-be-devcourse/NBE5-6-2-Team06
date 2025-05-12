package com.grepp.matnam.app.controller.api.user;

import com.grepp.matnam.app.controller.api.user.payload.JwtResponse;
import com.grepp.matnam.app.controller.api.user.payload.UserSigninRequest;
import com.grepp.matnam.app.controller.api.user.payload.UserSignupRequest;
import com.grepp.matnam.app.controller.api.user.payload.UserResponse;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.jwt.JwtTokenProvider;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "사용자 관리 API")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<ApiResponse> signup(@Validated @RequestBody UserSignupRequest request) {
        try {
            User user = new User();
            user.setUserId(request.getUserId());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setAddress(request.getAddress());
            user.setNickname(request.getNickname());
            user.setAge(request.getAge());
            user.setGender(request.getGender());

            User savedUser = userService.signup(user);

            UserResponse response = UserResponse.builder()
                    .userId(savedUser.getUserId())
                    .email(savedUser.getEmail())
                    .address(savedUser.getAddress())
                    .nickname(savedUser.getNickname())
                    .age(savedUser.getAge())
                    .gender(savedUser.getGender())
                    .temperature(savedUser.getTemperature())
                    .role(savedUser.getRole())
                    .build();

            String token = jwtTokenProvider.generateToken(savedUser.getUserId(), savedUser.getRole().name());

            JwtResponse jwtResponse = JwtResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .userId(savedUser.getUserId())
                    .role(savedUser.getRole().name())
                    .expiration(86400)
                    .build();

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "회원가입 성공", jwtResponse));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "회원가입 실패", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "회원가입 중 서버 오류가 발생했습니다.", e.getMessage()));
        }
    }

    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "사용자 로그인 후 JWT 토큰을 발급합니다.")
    public ResponseEntity<ApiResponse> signin(@Validated @RequestBody UserSigninRequest request) {
        try {
            User user = userService.signin(request.getUserId(), request.getPassword());

            String token = jwtTokenProvider.generateToken(user.getUserId(), user.getRole().name());

            JwtResponse jwtResponse = JwtResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .userId(user.getUserId())
                    .role(user.getRole().name())
                    .expiration(86400)
                    .build();

            return ResponseEntity.ok(new ApiResponse(ResponseCode.OK.code(), "로그인 성공", jwtResponse));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(ResponseCode.BAD_REQUEST.code(), "로그인 실패", e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR.code(), "로그인 중 서버 오류가 발생했습니다.", e.getMessage()));
        }
    }
}