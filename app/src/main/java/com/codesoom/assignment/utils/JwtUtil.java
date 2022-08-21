package com.codesoom.assignment.utils;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 암호화한 토큰을 생성하고 리턴합니다.
     *
     * @param userId 식별자
     * @param role 역할
     * @return 토큰
     */
    public String encode(Long userId, Role role) {
        return Jwts.builder()
                .setIssuer("BJP")
                .claim("userId", userId)
                .claim("role", role)
                .signWith(key)
                .compact();
    }

    /**
     * 복호화된 토큰의 정보를 추출해 리턴합니다.
     *
     * @param token 토큰
     * @return 정보
     * @throws InvalidTokenException 유효하지 않은 토큰
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
