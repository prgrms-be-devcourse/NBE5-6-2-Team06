package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.controller.api.user.payload.PreferenceRequest;
import com.grepp.matnam.app.model.user.entity.Preference;
import com.grepp.matnam.app.model.user.entity.User;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PreferenceService {

    @Autowired
    private UserRepository userRepository;

    private final PreferenceRepository preferenceRepository;

    public void savePreference(String userId, PreferenceRequest request) {
        log.info("사용자 ID로 취향 저장 시도: " + userId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();

        if (!userId.equals(currentUserId)) {
            log.info("요청된 사용자 ID와 현재 인증된 사용자 ID가 일치하지 않습니다. 요청=" + userId + ", 인증=" + currentUserId);
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.info("사용자를 찾을 수 없음: " + userId);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
                });

        log.info("사용자 발견: " + user.getUserId());

        Preference preference = user.getPreference();

        if (preference == null) {
            log.info("새 취향 객체 생성");
            preference = new Preference();
            preference.setUser(user);
            user.setPreference(preference);
        } else {
            log.info("기존 취향 객체 업데이트");
        }

        updatePreference(preference, request);

        User savedUser = userRepository.save(user);
        log.info("사용자 및 취향 저장 완료: " + savedUser.getUserId());
    }

    public void updatePreference(String userId, PreferenceRequest request) {
        log.info("사용자 ID로 취향 업데이트 시도: " + userId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();

        if (!userId.equals(currentUserId)) {
            log.info("요청된 사용자 ID와 현재 인증된 사용자 ID가 일치하지 않습니다. 요청=" + userId + ", 인증=" + currentUserId);
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.info("사용자를 찾을 수 없음: " + userId);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
                });

        log.info("사용자 발견: " + user.getUserId());

        Preference preference = user.getPreference();

        if (preference == null) {
            log.info("기존 취향 설정이 없음");
            throw new IllegalStateException("먼저 취향을 설정해야 합니다.");
        }

        updatePreference(preference, request);

        User savedUser = userRepository.save(user);
        log.info("사용자 및 취향 업데이트 완료: " + savedUser.getUserId());
    }

    private void updatePreference(Preference preference, PreferenceRequest request) {
        preference.setGoodTalk(request.isGoodTalk());
        preference.setManyDrink(request.isManyDrink());
        preference.setGoodMusic(request.isGoodMusic());
        preference.setClean(request.isClean());
        preference.setGoodView(request.isGoodView());
        preference.setTerrace(request.isTerrace());
        preference.setGoodPicture(request.isGoodPicture());
        preference.setGoodMenu(request.isGoodMenu());
        preference.setLongStay(request.isLongStay());
        preference.setBigStore(request.isBigStore());

        StringBuilder selectedPrefs = new StringBuilder("선택된 취향: ");
        if (request.isGoodTalk()) selectedPrefs.append("대화하기 좋은, ");
        if (request.isManyDrink()) selectedPrefs.append("술이 다양한, ");
        if (request.isGoodMusic()) selectedPrefs.append("음악이 좋은, ");
        if (request.isClean()) selectedPrefs.append("깨끗한, ");
        if (request.isGoodView()) selectedPrefs.append("뷰가 좋은, ");
        if (request.isTerrace()) selectedPrefs.append("테라스가 있는, ");
        if (request.isGoodPicture()) selectedPrefs.append("사진이 잘 나오는, ");
        if (request.isGoodMenu()) selectedPrefs.append("메뉴가 다양한, ");
        if (request.isLongStay()) selectedPrefs.append("오래 머물 수 있는, ");
        if (request.isBigStore()) selectedPrefs.append("매장이 넓은, ");
    }

    public Map<String, Long> getPreferenceCounts() {
        return preferenceRepository.getPreferenceCounts();
    }
}