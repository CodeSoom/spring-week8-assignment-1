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
 * Jwt의 인코딩과 디코딩 기능을 제공한다.
 */
@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 사용자의 식별자를 인자로 받아 Jwt 인코딩된 결과를 반환한다.
     * @param userId Jwt 발급 대상 식별자
     * @return Jwt 인코드 결과물
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    /**
     * Jwt를 디코딩해서 내부 정보를 반환한다.
     * @param token 디코딩할 문자열
     * @return Jwt의 내부 정보를 반환한다.
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
