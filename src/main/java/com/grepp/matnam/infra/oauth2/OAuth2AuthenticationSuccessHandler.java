package com.grepp.matnam.infra.oauth2;

import com.grepp.matnam.app.model.auth.code.Role;
import com.grepp.matnam.app.model.user.UserRepository;
import com.grepp.matnam.app.model.user.code.Gender;
import com.grepp.matnam.app.model.user.code.Status;
import com.grepp.matnam.app.model.user.dto.CustomOAuth2User;
import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String userId = oAuth2User.getUserId();
        String email = oAuth2User.getEmail();
        String name = oAuth2User.getName();
        String role = oAuth2User.getAuthorities().iterator().next().getAuthority();

        log.info("OAuth2 로그인 성공: userId={}, email={}, name={}", userId, email, name);

        String token = jwtTokenProvider.generateToken(userId, role);

        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setMaxAge(86400);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        Cookie userIdCookie = new Cookie("userId", userId);
        userIdCookie.setMaxAge(86400);
        userIdCookie.setPath("/");
        response.addCookie(userIdCookie);

        Cookie roleCookie = new Cookie("userRole", role);
        roleCookie.setMaxAge(86400);
        roleCookie.setPath("/");
        response.addCookie(roleCookie);

        boolean isExistingUser = userRepository.existsByUserId(userId);

        if (!isExistingUser) {
            log.info("신규 OAuth2 사용자: {}", userId);

            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setEmail(email);
            newUser.setNickname(name);
            newUser.setPassword(passwordEncoder.encode("1234"));
            newUser.setRole(Role.ROLE_USER);
            newUser.setStatus(Status.ACTIVE);
            newUser.setTemperature(36.5f);
            newUser.setGender(Gender.MAN);
            newUser.setAge(30);

            userRepository.save(newUser);
            log.info("OAuth2 사용자 기본 정보 저장: {}", userId);

            request.getSession().setAttribute("oauthEmail", email);
            request.getSession().setAttribute("oauthName", name);
            response.sendRedirect("/user/oauth2/signup");
        } else {
            User user = userRepository.findByUserId(userId).orElse(null);
            if (user != null && user.getPreference() == null) {
                response.sendRedirect("/user/preference");
            } else {
                response.sendRedirect("/");
            }
        }
    }
}