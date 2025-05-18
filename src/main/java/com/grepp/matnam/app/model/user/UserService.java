package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.controller.api.admin.payload.AgeDistributionResponse;
import com.grepp.matnam.app.controller.api.admin.payload.UserStatusRequest;
import com.grepp.matnam.app.facade.NotificationSender;
import com.grepp.matnam.app.model.notification.code.NotificationType;
import com.grepp.matnam.app.model.notification.entity.Notice;
import com.grepp.matnam.app.model.notification.entity.Notification;
import com.grepp.matnam.app.model.notification.repository.NotificationRepository;
import com.grepp.matnam.app.model.notification.service.NotificationService;
import com.grepp.matnam.app.model.sse.SseService;
import com.grepp.matnam.infra.response.Messages;
import com.grepp.matnam.app.controller.web.admin.payload.TotalUserResponse;
import com.grepp.matnam.app.controller.web.admin.payload.UserStatsResponse;
import com.grepp.matnam.app.model.auth.code.Role;
import com.grepp.matnam.app.model.user.code.Gender;
import com.grepp.matnam.app.model.user.code.Status;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.auth.PasswordValidator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final NotificationSender notificationSender;

    public User signup(User user) {
        log.info("회원가입 시도: {}, 이메일: {}", user.getUserId(), user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            log.info("이메일 중복 발생: {}", user.getEmail());
            throw new IllegalArgumentException(Messages.USER_EMAIL_DUPLICATE);
        }

        if (userRepository.existsByUserId(user.getUserId())) {
            log.info("아이디 중복 발생: {}", user.getUserId());
            throw new IllegalArgumentException(Messages.USER_ID_DUPLICATE);
        }

        PasswordValidator.validate(user.getPassword());

        log.info("비밀번호 암호화 진행");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(Role.ROLE_USER);
        user.setStatus(Status.ACTIVE);
        user.setTemperature(36.5f);
        log.info("사용자 기본 설정 완료: 역할={}, 상태={}", Role.ROLE_USER, Status.ACTIVE);

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: {}", savedUser.getUserId());

        return savedUser;
    }

    @Transactional(readOnly = true)
    public User signin(String userId, String password) {
        log.info("로그인 시도: {}", userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.info("로그인 실패: 사용자 없음 - {}", userId);
                    return new IllegalArgumentException(Messages.USER_NOT_FOUND);
                });

        if (user.getStatus() == Status.SUSPENDED || user.getStatus() == Status.BANNED) {
            log.info("로그인 실패: 정지된 계정 - {}, 상태={}", userId, user.getStatus());
            throw new IllegalArgumentException(Messages.USER_SUSPENDED);
        }

        if (user.getStatus() != Status.ACTIVE || !user.isActivated()) {
            log.info("로그인 실패: 비활성화된 계정 - {}, 상태={}", userId, user.getStatus());
            throw new IllegalArgumentException(Messages.USER_INACTIVE);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("로그인 실패: 비밀번호 불일치 - {}", userId);
            throw new IllegalArgumentException(Messages.USER_PASSWORD_MISMATCH);
        }

        log.info("로그인 성공: {}", userId);
        return user;
    }

    public User deactivateAccount(String userId, String password) {
        log.info("회원 탈퇴 시도: {}", userId);

        User user = getUserByIdWithValidation(userId);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("회원 탈퇴 실패: 비밀번호 불일치 - {}", userId);
            throw new IllegalArgumentException(Messages.USER_PASSWORD_MISMATCH);
        }

        user.unActivated();

        User updatedUser = userRepository.save(user);
        log.info("회원 탈퇴 완료: {}", updatedUser.getUserId());

        return updatedUser;
    }

    @Transactional(readOnly = true)
    public User getUserById(String userId) {
        log.info("사용자 정보 조회: {}", userId);
        return getUserByIdWithValidation(userId);
    }

    private User getUserByIdWithValidation(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.info("사용자 정보 조회 실패: 사용자 없음 - {}", userId);
                    return new IllegalArgumentException(Messages.USER_NOT_FOUND);
                });
    }

    public User changePassword(String userId, String currentPassword, String newPassword) {
        log.info("비밀번호 변경 시도: {}", userId);

        User user = getUserByIdWithValidation(userId);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            log.info("비밀번호 변경 실패: 현재 비밀번호 불일치 - {}", userId);
            throw new IllegalArgumentException(Messages.USER_CURRENT_PASSWORD_MISMATCH);
        }

        PasswordValidator.validate(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));

        User updatedUser = userRepository.save(user);
        log.info("비밀번호 변경 완료: {}", updatedUser.getUserId());

        return updatedUser;
    }

    public Page<User> findByFilter(String status, String keyword, Pageable pageable) {
        if (!status.isBlank() && StringUtils.hasText(keyword)) {
            return userRepository.findByStatusAndKeywordContaining(status, keyword, pageable);
        } else if (!status.isBlank()) {
            return userRepository.findByStatus(status, pageable);
        } else if (StringUtils.hasText(keyword)) {
            return userRepository.findByKeywordContaining(keyword, pageable);
        } else {
            return userRepository.findAllUsers(pageable);
        }
    }

    public void updateUserStatus(String userId, UserStatusRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setStatus(request.getStatus());

        if (request.getStatus() == Status.SUSPENDED) {
            user.setSuspendDuration(request.getSuspendDuration());
            user.setDueDate(LocalDate.now().plusDays(request.getSuspendDuration()));
            user.setDueReason(request.getDueReason());
        } else if (request.getStatus() == Status.BANNED){
            user.setDueReason(request.getDueReason());
        } else {
            user.setSuspendDuration(null);
            user.setDueDate(null);
            user.setDueReason(null);
        }

        userRepository.save(user);
    }

    public void unActivatedById(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.unActivated();
        userRepository.save(user);
    }

    @Transactional
    public User updateOAuth2User(User user) {
        log.info("OAuth2 사용자 정보 저장: {}", user.getUserId());

        Optional<User> existingUser = userRepository.findByUserId(user.getUserId());

        if (existingUser.isPresent()) {
            User existing = existingUser.get();
            existing.setNickname(user.getNickname());
            existing.setAddress(user.getAddress());
            existing.setAge(user.getAge());
            existing.setGender(user.getGender());
            return userRepository.save(existing);
        } else {
            return userRepository.save(user);
        }
    }

    public TotalUserResponse getTotalUserStats() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        long totalUsers = userRepository.count(); // 현재 전체 회원 수
        long yesterdayUserCount = userRepository.countByCreatedAtBefore(LocalDateTime.of(today, LocalTime.MIDNIGHT));
        String userGrowth = calculateGrowthRate(totalUsers, yesterdayUserCount);
        return new TotalUserResponse(totalUsers, userGrowth);
    }

    private String calculateGrowthRate(long today, long yesterday) {
        if (yesterday == 0) {
            if (today == 0) return "+0%";
            return "+100%"; // 또는 "N/A"
        }
        long diff = today - yesterday;
        double percent = ((double) diff / yesterday) * 100;
        String sign = percent >= 0 ? "+" : "";
        return String.format("%s%.0f%%", sign, percent);
    }

    public List<AgeDistributionResponse> getAgeDistribution() {
        List<Integer> ages = userRepository.findAllAges(); // 모든 활성 사용자의 나이를 가져옴

        // 연령대 정의 (순서 보장을 위해 LinkedHashMap 사용)
        Map<String, Long> ageGroupCounts = new LinkedHashMap<>();
        ageGroupCounts.put("10대", 0L); // 10-19
        ageGroupCounts.put("20대", 0L); // 20-29
        ageGroupCounts.put("30대", 0L); // 30-39
        ageGroupCounts.put("40대", 0L); // 40-49
        ageGroupCounts.put("50대", 0L); // 50-59
        ageGroupCounts.put("60대 이상", 0L); // 60+

        for (int age : ages) {
            if (age >= 10 && age <= 19) {
                ageGroupCounts.put("10대", ageGroupCounts.get("10대") + 1);
            } else if (age >= 20 && age <= 29) {
                ageGroupCounts.put("20대", ageGroupCounts.get("20대") + 1);
            } else if (age >= 30 && age <= 39) {
                ageGroupCounts.put("30대", ageGroupCounts.get("30대") + 1);
            } else if (age >= 40 && age <= 49) {
                ageGroupCounts.put("40대", ageGroupCounts.get("40대") + 1);
            } else if (age >= 50 && age <= 59) {
                ageGroupCounts.put("50대", ageGroupCounts.get("50대") + 1);
            } else if (age >= 60) {
                ageGroupCounts.put("60대 이상", ageGroupCounts.get("60대 이상") + 1);
            }
        }

        return ageGroupCounts.entrySet().stream()
            .map(entry -> new AgeDistributionResponse(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    public Map<Gender, Long> getGenderDistribution() {
        List<Gender> genders = userRepository.findAllGenders();
        Map<Gender, Long> genderCounts = new HashMap<>();
        genderCounts.put(Gender.MAN, genders.stream().filter(gender -> gender == Gender.MAN).count());
        genderCounts.put(Gender.WOMAN, genders.stream().filter(gender -> gender == Gender.WOMAN).count());
        return genderCounts;
    }

    public UserStatsResponse getUserStatistics() {

        UserStatsResponse statsResponse = new UserStatsResponse();

        statsResponse.setTotalUsers(userRepository.count());
        statsResponse.setActivatedUsers(userRepository.countByActivated(true));
        statsResponse.setNewUsers(userRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(30)));
        statsResponse.setStopUsers(userRepository.countByStatusNotActive());
        statsResponse.setInactivatedUsers(userRepository.countByActivated(false));

        statsResponse.setTotalMaleUsers(userRepository.countByGender(Gender.MAN));
        statsResponse.setActivatedMaleUsers(userRepository.countByGenderAndActivated(Gender.MAN, true));
        statsResponse.setNewMaleUsers(userRepository.countByGenderAndCreatedAtAfter(Gender.MAN, LocalDateTime.now().minusDays(30)));
        statsResponse.setStopMaleUsers(userRepository.countByGenderAndStatusNotActive(Gender.MAN));
        statsResponse.setInactivatedMaleUsers(userRepository.countByGenderAndActivated(Gender.MAN, false));

        statsResponse.setTotalFemaleUsers(userRepository.countByGender(Gender.WOMAN));
        statsResponse.setActivatedFemaleUsers(userRepository.countByGenderAndActivated(Gender.WOMAN, true));
        statsResponse.setNewFemaleUsers(userRepository.countByGenderAndCreatedAtAfter(Gender.WOMAN, LocalDateTime.now().minusDays(30)));
        statsResponse.setStopFemaleUsers(userRepository.countByGenderAndStatusNotActive(Gender.WOMAN));
        statsResponse.setInactivatedFemaleUsers(userRepository.countByGenderAndActivated(Gender.WOMAN, false));


        return statsResponse;
    }

    @Transactional
    public void sendBroadcastNotification(String content) {
        // ROLE_USER 이고 활성화된 사용자만 조회
        List<User> usersToSend = userRepository.findByRoleEqualsAndActivatedIsTrue(Role.ROLE_USER);
        notificationService.saveNotice(content, null);

        for (User user : usersToSend) {
            notificationSender.sendNotificationToUser(user.getUserId(), NotificationType.NOTICE, content, null);
        }
    }

    public void resendBroadcastNotification(Long noticeId) {
        List<User> usersToSend = userRepository.findByRoleEqualsAndActivatedIsTrue(Role.ROLE_USER);
        Notice notice = notificationService.getNotice(noticeId);

        for (User user : usersToSend) {
            notificationSender.resendNotificationToUser(user.getUserId(), notice);
        }
    }
}