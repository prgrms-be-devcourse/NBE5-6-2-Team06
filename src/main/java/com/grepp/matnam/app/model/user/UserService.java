package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.app.model.user.code.Status;
import com.grepp.matnam.app.model.auth.code.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signup(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (userRepository.existsByUserId(user.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(Role.ROLE_USER);
        user.setStatus(Status.ACTIVE);
        user.setTemperature(36.5f);

        return userRepository.save(user);
    }
}