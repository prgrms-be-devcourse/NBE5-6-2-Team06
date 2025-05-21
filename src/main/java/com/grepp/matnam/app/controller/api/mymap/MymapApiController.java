package com.grepp.matnam.app.controller.api.mymap;

import com.grepp.matnam.app.model.mymap.MymapService;
import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    @Operation(
            summary = "내가 저장한 장소 목록 조회",
            description = "로그인한 사용자가 공개 상태로 표시한 장소 리스트를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장소 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public List<Mymap> getMyPinnedPlaces() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        return mymapService.getPinnedPlaces(user);
    }

    @GetMapping("/activated")
    @Operation(
            summary = "내가 활성화한 장소 목록 조회",
            description = "로그인한 사용자가 공개/비공개 상태로 표시한 장소 리스트를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장소 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public List<Mymap> getMyActivatedPlaces() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        return mymapService.getActivatedPlaces(user);
    }

    @PostMapping
    @Operation(
            summary = "장소 저장",
            description = "새로운 장소를 저장하며 기본으로 활성화 및 공개 상태로 설정됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "장소 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public String savePlace(@RequestBody Mymap mymap) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        mymap.setUser(user);
        mymap.setActivated(true);
        mymap.setPinned(true);
        mymapService.savePlace(mymap);
        return "저장 완료";
    }

    @PatchMapping("/{id}/pinned")
    @Operation(
            summary = "장소의 공개 여부(Pinned) 수정",
            description = "장소의 핀 여부를 업데이트하여 공개/비공개 상태를 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "핀 상태 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. pinned 값 누락"),
            @ApiResponse(responseCode = "404", description = "해당 장소를 찾을 수 없음")
    })
    public ResponseEntity<String> updatePinnedStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean isPinned = body.get("pinned");
        if (isPinned == null) {
            return ResponseEntity.badRequest().body("공개 여부 값이 필요합니다.");
        }

        try {
            mymapService.updatePinnedStatus(id, isPinned);
            return ResponseEntity.ok(isPinned ? "공개로 설정되었습니다." : "숨김 처리되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 장소를 찾을 수 없습니다.");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "장소 삭제 처리",
            description = "장소의 활성화 상태를 false로 설정하여 삭제 처리합니다. 실제 DB에서 삭제되지는 않습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장소 삭제 처리 완료"),
            @ApiResponse(responseCode = "404", description = "삭제할 장소를 찾을 수 없음")
    })
    public ResponseEntity<String> updateActivatedStatus(@PathVariable Long id) {
        Mymap place = mymapService.findById(id);
        if (place == null || !place.getActivated()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 장소를 찾을 수 없습니다.");
        }

        mymapService.updateActivatedStatus(id, false);
        return ResponseEntity.ok("장소가 삭제 처리되었습니다.");
    }
}
