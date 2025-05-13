package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.auth.code.Role;
import com.grepp.matnam.app.model.user.code.Status;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signup(User user) {
        log.info("회원가입 시도: " + user.getUserId() + ", 이메일: " + user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            log.info("이메일 중복 발생: " + user.getEmail());
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (userRepository.existsByUserId(user.getUserId())) {
            log.info("아이디 중복 발생: " + user.getUserId());
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        log.info("비밀번호 암호화 진행");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(Role.ROLE_USER);
        user.setStatus(Status.ACTIVE);
        user.setTemperature(36.5f);
        log.info("사용자 기본 설정 완료: 역할=" + Role.ROLE_USER + ", 상태=" + Status.ACTIVE);

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: " + savedUser.getUserId());

        return savedUser;
    }

    public User signin(String userId, String password) {
        log.info("로그인 시도: " + userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.info("로그인 실패: 사용자 없음 - " + userId);
                    return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                });

        if (user.getStatus() == Status.SUSPENDED || user.getStatus() == Status.BANNED) {
            log.info("로그인 실패: 정지된 계정 - " + userId + ", 상태=" + user.getStatus());
            throw new IllegalArgumentException("정지된 계정입니다.");
        }

        if (user.getStatus() != Status.ACTIVE || !user.isActivated()) {
            log.info("로그인 실패: 비활성화된 계정 - " + userId + ", 상태=" + user.getStatus());
            throw new IllegalArgumentException("비활성화(탈퇴) 계정입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("로그인 실패: 비밀번호 불일치 - " + userId);
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        log.info("로그인 성공: " + userId);
        return user;
    }

    public User deactivateAccount(String userId, String password) {
        log.info("회원 탈퇴 시도: " + userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.info("회원 탈퇴 실패: 사용자 없음 - " + userId);
                    return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("회원 탈퇴 실패: 비밀번호 불일치 - " + userId);
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        user.unActivated();

        User updatedUser = userRepository.save(user);
        log.info("회원 탈퇴 완료: " + updatedUser.getUserId());

        return updatedUser;
    }

    public User getUserById(String userId) {
        log.info("사용자 정보 조회: " + userId);
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.info("사용자 정보 조회 실패: 사용자 없음 - " + userId);
                    return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                });
    }

    public User changePassword(String userId, String currentPassword, String newPassword) {
        log.info("비밀번호 변경 시도: " + userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.info("비밀번호 변경 실패: 사용자 없음 - " + userId);
                    return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                });

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            log.info("비밀번호 변경 실패: 현재 비밀번호 불일치 - " + userId);
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        User updatedUser = userRepository.save(user);
        log.info("비밀번호 변경 완료: " + updatedUser.getUserId());

        return updatedUser;
    }
}