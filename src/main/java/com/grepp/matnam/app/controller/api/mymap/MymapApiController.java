package com.grepp.matnam.app.controller.api.mymap;

import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.mymap.MymapRepository;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mymap")
public class MymapApiController {

    private final MymapRepository mymapRepository;
    private final UserService userService;

    // 현재 로그인한 사용자의 pinned 맛집 리스트 조회
    @GetMapping("/mine")
    public List<Mymap> getMyPinnedPlaces() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);
        return mymapRepository.findByUserAndPinnedTrue(user);
    }

    // 맛집 등록
    @PostMapping
    public String saveMyPlace(@RequestBody Mymap mymap) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(userId);

        mymap.setUser(user);
        mymap.setPinned(true);
        mymapRepository.save(mymap);

        return "저장 완료";
    }

//    // 맛집 삭제
//    @DeleteMapping("/{id}")
//    public String deletePlace(@PathVariable Long id) {
//        mymapRepository.deleteById(id);
//        return "삭제 완료";
//    }
}
