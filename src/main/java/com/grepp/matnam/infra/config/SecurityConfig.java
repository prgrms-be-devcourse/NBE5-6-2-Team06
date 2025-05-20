package com.grepp.matnam.infra.config;

import com.grepp.matnam.app.model.user.CustomOauth2UserService;
import com.grepp.matnam.infra.jwt.JwtAuthenticationFilter;
import com.grepp.matnam.infra.oauth2.OAuth2AuthenticationSuccessHandler;
import com.grepp.matnam.infra.security.CustomAccessDeniedHandler;
import com.grepp.matnam.infra.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomOauth2UserService customOauth2UserService,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customOauth2UserService = customOauth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin()
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        // 1. 인증이 필요하지 않은 경로
                        .requestMatchers("/", "/error", "/favicon.ico").permitAll()
                        .requestMatchers("/team/search", "/team/detail/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()

                        // 회원가입/로그인 관련
                        .requestMatchers("/user/signup", "/user/signin", "/user/oauth2/signup").permitAll()
                        .requestMatchers("/api/user/signup", "/api/user/signin").permitAll()

                        // OAuth2 관련
                        .requestMatchers("/signin/oauth2/code/**").permitAll()

                        // Swagger/API 문서 관련
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // AI 테스트 관련
                        .requestMatchers("/api/ai/chat", "/api/ai/restaurant/name").permitAll()

                        // 2. 관리자 권한이 필요한 경로
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")

                        // 3. 사용자 권한이 필요한 경로
                        // 사용자 관련
                        .requestMatchers("/user/**").hasRole("USER")

                        // 채팅 관련
                        .requestMatchers("/api/chat/**").hasAnyRole("USER","ADMIN")

                        // 모임/팀 관련
                        .requestMatchers("/team/**", "/api/team/**").authenticated()

                        // 지도/장소 관련
                        .requestMatchers("/map/**", "/api/mymap/**").hasRole("USER")

                        // 리뷰 관련
                        .requestMatchers("/api/reviews/**").hasRole("USER")

                        // AI 추천 관련
                        .requestMatchers("/api/ai/recommend/**", "/api/ai/reRecommend/**").hasRole("USER")

                        // 랭킹 관련
                        .requestMatchers("/api/content-rankings/**").hasAnyRole("USER", "ADMIN")

                        // 신고 기능
                        .requestMatchers("/api/reports/**").hasRole("USER")

                        // 알림 기능
                        .requestMatchers("/api/sse/subscribe").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/notification/unread-count").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/notification").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/notification/unread").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/notification/system").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/notification/mark-read").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/notification/{notificationId}").hasAnyRole("USER", "ADMIN")

                        // 4. 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(customOauth2UserService)
                        )
                        .redirectionEndpoint(endpoint -> endpoint
                                .baseUri("/signin/oauth2/code/*")
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}