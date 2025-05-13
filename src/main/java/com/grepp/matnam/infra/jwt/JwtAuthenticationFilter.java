package com.grepp.matnam.infra.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            logger.info("요청 URI: " + requestURI);

            String jwt = parseJwt(request);

            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                String userId = tokenProvider.getUserIdFromJwtToken(jwt);
                String role = tokenProvider.getRoleFromJwtToken(jwt);

                logger.info("JWT 인증 성공: 사용자 ID = " + userId + ", 역할 = " + role);

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("인증 객체 SecurityContext에 설정 완료");
            } else if (jwt != null) {
                logger.info("JWT 토큰 검증 실패");
            }
        } catch (Exception e) {
            logger.error("JWT 인증 실패: " + e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            logger.info("Authorization 헤더에서 토큰 찾음");
            return headerAuth.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    logger.info("쿠키에서 토큰 찾음: " + cookie.getValue().substring(0, 10) + "...");
                    return cookie.getValue();
                }
            }
        }

        logger.info("JWT 토큰을 찾을 수 없음");
        return null;
    }
}