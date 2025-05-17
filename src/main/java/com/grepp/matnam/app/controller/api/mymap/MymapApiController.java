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

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mymap")
public class MymapApiController {

    private final MymapService mymapService;
    private final UserService userService;

    @GetMapping("/mine")
    public List<Mymap> getMyPinnedPlaces() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        return mymapService.getPinnedPlaces(user);
    }

    @GetMapping("/activated")
    public List<Mymap> getMyActivatedPlaces() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        return mymapService.getActivatedPlaces(user);
    }

    @PostMapping
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
    public ResponseEntity<String> updateActivatedStatus(@PathVariable Long id) {
        Mymap place = mymapService.findById(id);
        if (place == null || !place.getActivated()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 장소를 찾을 수 없습니다.");
        }

        mymapService.updateActivatedStatus(id, false);
        return ResponseEntity.ok("장소가 삭제 처리되었습니다.");
    }
}