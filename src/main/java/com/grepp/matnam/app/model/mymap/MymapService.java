package com.grepp.matnam.app.model.mymap;

import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MymapService {

    private final MymapRepository mymapRepository;

    @Transactional(readOnly = true)
    public List<Mymap> getPinnedPlaces(User user) {
        return mymapRepository.findByUserAndPinnedTrue(user);
    }

    @Transactional(readOnly = true)
    public List<Mymap> getActivatedPlaces(User user) {
        return mymapRepository.findByUserAndActivatedTrue(user);
    }

    @Transactional(readOnly = true)
    public Mymap findById(Long id) {
        return mymapRepository.findById(id).orElse(null);
    }

    @Transactional
    public Mymap savePlace(Mymap mymap) {
        return mymapRepository.save(mymap);
    }

    @Transactional
    public void updatePinnedStatus(Long id, boolean isPinned) {
        Mymap place = mymapRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다."));
        place.setPinned(isPinned);
        mymapRepository.save(place);
    }

    @Transactional
    public void updateActivatedStatus(Long id, boolean isActivated) {
        Mymap place = mymapRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다."));
        place.setActivated(isActivated);
        mymapRepository.save(place);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getPlaceCounts(User user) {
        long total = mymapRepository.countByUserAndActivatedTrue(user);
        long visible = mymapRepository.countByUserAndActivatedTrueAndPinnedTrue(user);
        long hidden = mymapRepository.countByUserAndActivatedTrueAndPinnedFalse(user);

        Map<String, Long> result = new HashMap<>();
        result.put("total", total);
        result.put("visible", visible);
        result.put("hidden", hidden);
        return result;
    }
}
