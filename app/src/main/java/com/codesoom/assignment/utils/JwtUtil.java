package com.codesoom.assignment.utils;

import com.codesoom.assignment.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * 컴포넌트 스캔의 대상 지정
 */
@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode(Long id) {
        return Jwts.builder()
                .claim("id", id)
                .signWith(key)
                .compact();
    }

    public Claims decode(String token) throws InvalidTokenException {
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
