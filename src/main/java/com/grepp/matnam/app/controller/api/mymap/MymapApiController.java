package com.grepp.matnam.app.controller.api.mymap;

import com.grepp.matnam.app.model.mymap.MymapService;
import com.grepp.matnam.app.model.mymap.dto.MymapRequestDto;
import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mymap")
@Tag(name = "Mymap API", description = "사용자의 개인 맛집 지도 관련 API")
public class MymapApiController {

    private final MymapService mymapService;
    private final UserService userService;

    @GetMapping("/mine")
    @Operation(summary = "공개된 내 장소 목록 조회", description = "공개 설정된 장소만 반환합니다.")
    public ResponseEntity<ApiResponse<List<Mymap>>> getMyPinnedPlaces() {
        String userId = getCurrentUserId();
        User user = userService.getUserById(userId);
        List<Mymap> places = mymapService.getPinnedPlaces(user);
        return ResponseEntity.status(ResponseCode.OK.status())
                .body(ApiResponse.success(places));
    }

    @GetMapping("/activated")
    @Operation(summary = "내 장소 전체 조회", description = "공개/비공개 여부와 상관없이 활성화된 모든 장소를 반환합니다.")
    public ResponseEntity<ApiResponse<List<Mymap>>> getMyActivatedPlaces(@RequestParam(required = false) Boolean pinned) {
        String userId = getCurrentUserId();
        User user = userService.getUserById(userId);
        List<Mymap> places = mymapService.getFilteredActivatedPlaces(user, pinned);
        return ResponseEntity.status(ResponseCode.OK.status())
                .body(ApiResponse.success(places));
    }

    @PostMapping
    @Operation(summary = "장소 저장", description = "새로운 장소를 저장하며 기본적으로 활성화된 상태입니다.")
    public ResponseEntity<ApiResponse<String>> savePlace(@RequestBody @Valid MymapRequestDto dto) {
        String userId = getCurrentUserId();
        User user = userService.getUserById(userId);
        mymapService.savePlace(dto, user);
        return ResponseEntity.status(ResponseCode.OK.status())
                .body(ApiResponse.success("장소가 저장되었습니다."));
    }

    @PatchMapping("/{id}/pinned")
    @Operation(summary = "장소 공개 여부 수정", description = "pinned 값에 따라 공개 또는 숨김 상태로 변경합니다.")
    public ResponseEntity<ApiResponse<String>> updatePinnedStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean isPinned = body.get("pinned");
        if (isPinned == null) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
                    .body(ApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        try {
            mymapService.updatePinnedStatus(id, isPinned);
            return ResponseEntity.status(ResponseCode.OK.status())
                    .body(ApiResponse.success(isPinned ? "공개로 설정되었습니다." : "숨김 처리되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
                    .body(ApiResponse.error(ResponseCode.BAD_REQUEST));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "장소 삭제", description = "논리적 삭제: 활성화 상태를 false로 전환합니다.")
    public ResponseEntity<ApiResponse<String>> updateActivatedStatus(@PathVariable Long id) {
        Mymap place = mymapService.findById(id);
        if (place == null || !place.getActivated()) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
                    .body(ApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        mymapService.updateActivatedStatus(id, false);
        return ResponseEntity.status(ResponseCode.OK.status())
                .body(ApiResponse.success("장소가 삭제 처리되었습니다."));
    }

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
