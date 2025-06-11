package com.grepp.matnam.app.controller.api.mymap;

import com.grepp.matnam.app.model.mymap.MymapService;
import com.grepp.matnam.app.model.mymap.dto.MymapRequestDto;
import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
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
public class MymapApiController {

    private final MymapService mymapService;
    private final UserService userService;

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<Mymap>>> getMyPinnedPlaces() {
        String userId = getCurrentUserId();
        User user = userService.getUserById(userId);
        List<Mymap> places = mymapService.getPinnedPlaces(user);
        return ResponseEntity
                .status(ResponseCode.OK.status())
                .body(ApiResponse.success(places));
    }

    @GetMapping("/activated")
    public ResponseEntity<ApiResponse<List<Mymap>>> getMyActivatedPlaces(@RequestParam(required = false) Boolean pinned) {
        String userId = getCurrentUserId();
        User user = userService.getUserById(userId);
        List<Mymap> places = mymapService.getFilteredActivatedPlaces(user, pinned);
        return ResponseEntity
                .status(ResponseCode.OK.status())
                .body(ApiResponse.success(places));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> savePlace(@RequestBody @Valid MymapRequestDto dto) {
        String userId = getCurrentUserId();
        User user = userService.getUserById(userId);
        mymapService.savePlace(dto, user);
        return ResponseEntity
                .status(ResponseCode.OK.status())
                .body(ApiResponse.success("장소가 저장되었습니다."));
    }

    @PatchMapping("/{id}/pinned")
    public ResponseEntity<ApiResponse<String>> updatePinnedStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean isPinned = body.get("pinned");
        if (isPinned == null) {
            return ResponseEntity
                    .status(ResponseCode.BAD_REQUEST.status())
                    .body(ApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        mymapService.updatePinnedStatus(id, isPinned);
        return ResponseEntity
                .status(ResponseCode.OK.status())
                .body(ApiResponse.success(isPinned ? "공개로 설정되었습니다." : "숨김 처리되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateActivatedStatus(@PathVariable Long id) {
        Mymap place = mymapService.findById(id);
        if (place == null || !place.getActivated()) {
            return ResponseEntity
                    .status(ResponseCode.BAD_REQUEST.status())
                    .body(ApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        mymapService.updateActivatedStatus(id, false);
        return ResponseEntity
                .status(ResponseCode.OK.status())
                .body(ApiResponse.success("장소가 삭제 처리되었습니다."));
    }

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}