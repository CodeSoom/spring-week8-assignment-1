package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰 관련 처리를 담당합니다.
 */
@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 회원 아이디를 암호화한 토큰을 리턴합니다.
     *
     * @param userId 회원 아이디
     * @return 암호화된 토큰
     */
    public String encode(Long userId) {
        return Jwts.builder()
            .claim("userId", userId)
            .signWith(key)
            .compact();
    }

    /**
     * 토큰을 해석하여 사용자 정보를 담고 있는 객체를 리턴합니다.
     *
     * @param token 해석할 토큰
     * @return 사용자 정보를 가진 객체
     * @throws InvalidTokenException 토큰이 올바르지 않은 경우
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
