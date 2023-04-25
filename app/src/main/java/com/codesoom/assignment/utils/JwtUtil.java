package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * Util For JWT
 *
 * @author sim
 */
@Component
public class JwtUtil {
    private final Key key;

    /**
     * JwtUtil에 대한 생성자 메서드
     *
     * @param secret - 시크릿 키
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * JWT 토큰 생성
     *
     * @param userId - 유저 고유 식별 ID
     * @return JWT Token
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    /**
     * JWT Token 디코딩 및 Claims 추출
     *
     * @param token - JWT Token
     * @return JWT Token에 들어있던 Claims 객체
     * @throws SignatureException - 디코딩 중 에러가 발생할 경우 예외 발생.
     */
    public Claims decode(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }
}
