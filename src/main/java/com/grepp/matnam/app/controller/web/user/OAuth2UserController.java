package com.grepp.matnam.app.controller.web.user;

import com.grepp.matnam.app.model.auth.code.Role;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.code.Gender;
import com.grepp.matnam.app.model.user.code.Status;
import com.grepp.matnam.app.model.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/user/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signupPage(HttpServletRequest request, Model model) {
        String email = (String) request.getSession().getAttribute("oauthEmail");
        String name = (String) request.getSession().getAttribute("oauthName");

        if (email == null || name == null) {
            log.warn("OAuth 세션 정보 없음");
            return "redirect:/user/signin";
        }

        model.addAttribute("email", email);
        model.addAttribute("nickname", name);

        return "user/oauth2-signup";
    }

    @PostMapping("/signup")
    public String submitSignup(
            @RequestParam String nickname,
            @RequestParam String address,
            @RequestParam int age,
            @RequestParam String gender,
            HttpServletRequest request) {

        String email = (String) request.getSession().getAttribute("oauthEmail");

        if (email == null) {
            log.warn("OAuth 세션 정보 없음");
            return "redirect:/user/signin";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        log.info("OAuth2 회원가입: userId={}, email={}, nickname={}", userId, email, nickname);

        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setNickname(nickname);
        user.setAddress(address);
        user.setAge(age);
        user.setGender(Gender.valueOf(gender));
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole(Role.ROLE_USER);
        user.setStatus(Status.ACTIVE);
        user.setTemperature(36.5f);

        userService.updateOAuth2User(user);

        request.getSession().removeAttribute("oauthEmail");
        request.getSession().removeAttribute("oauthName");

        return "redirect:/user/preference";
    }
}