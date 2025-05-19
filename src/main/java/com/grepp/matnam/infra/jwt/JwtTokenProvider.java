package com.grepp.matnam.infra.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret:mySecretKeyForJWTTokenValidation1234567890123456789012345678901234567890}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400}")
    private int jwtExpirationMs;

    /**
     * JWT 토큰 생성
     */
    public String generateToken(String userId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs * 1000);

        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 ID 추출
     */
    public String getUserIdFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            log.error("JWT 토큰에서 사용자 ID 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     * JWT 토큰에서 역할 추출
     */
    public String getRoleFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();

            return claims.get("role", String.class);
        } catch (Exception e) {
            log.error("JWT 토큰에서 역할 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     * JWT 토큰 유효성 검증
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("비어있는 JWT 토큰: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT 토큰 검증 중 오류 발생: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 토큰의 만료 시간을 확인
     */
    public long getExpirationTime(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();

            Date expiration = claims.getExpiration();
            return expiration.getTime();
        } catch (Exception e) {
            log.error("토큰 만료 시간 확인 실패: {}", e.getMessage());
            return 0;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}