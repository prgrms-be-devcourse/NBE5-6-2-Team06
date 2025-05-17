package com.grepp.matnam.app.controller.api.mymap;

import com.grepp.matnam.app.model.mymap.MymapService;
import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.mymap.MymapRepository;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public String saveMyPlace(@RequestBody Mymap mymap) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        mymap.setUser(user);
        mymap.setActivated(true);
        mymap.setPinned(true);
        mymapService.savePlace(mymap);
        return "저장 완료";
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePlace(@PathVariable Long id, @RequestBody Mymap request) {
        Mymap existing = mymapService.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 장소를 찾을 수 없습니다.");
        }

        if (request.getPinned() != null) {
            existing.setPinned(request.getPinned());
        }
        if (request.getActivated() != null) {
            existing.setActivated(request.getActivated());
        }

        mymapService.savePlace(existing);
        return ResponseEntity.ok("업데이트 완료");
    }
}

