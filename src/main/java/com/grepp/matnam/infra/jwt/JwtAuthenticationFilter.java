package com.grepp.matnam.infra.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            log.debug("요청 URI: {}", requestURI);

            if (isPublicResource(requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = parseJwt(request);

            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                String userId = tokenProvider.getUserIdFromJwtToken(jwt);
                String role = tokenProvider.getRoleFromJwtToken(jwt);

                if (userId != null && role != null) {
                    log.debug("JWT 인증 성공: 사용자 ID = {}, 역할 = {}", userId, role);

                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("인증 객체 SecurityContext에 설정 완료");
                }
            } else if (jwt != null) {
                log.debug("JWT 토큰 검증 실패");
            }
        } catch (Exception e) {
            log.error("JWT 인증 실패: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicResource(String uri) {
        return uri.startsWith("/css/") ||
                uri.startsWith("/js/") ||
                uri.startsWith("/images/") ||
                uri.startsWith("/fonts/") ||
                uri.startsWith("/assets/") ||
                uri.equals("/") ||
                uri.equals("/error") ||
                uri.startsWith("/user/signup") ||
                uri.startsWith("/user/signin") ||
                uri.startsWith("/user/oauth2/signup") ||
                uri.startsWith("/api/user/signup") ||
                uri.startsWith("/api/user/signin") ||
                uri.startsWith("/signin/oauth2/code/") ||
                uri.startsWith("/swagger-ui") ||
                uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/api/ai/chat") ||
                uri.startsWith("/api/ai/restaurant/name");
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            log.debug("Authorization 헤더에서 토큰 찾음");
            return headerAuth.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    log.debug("쿠키에서 토큰 찾음");
                    return cookie.getValue();
                }
            }
        }

        log.debug("JWT 토큰을 찾을 수 없음");
        return null;
    }
}