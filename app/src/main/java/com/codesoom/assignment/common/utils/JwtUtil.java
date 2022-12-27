package com.codesoom.assignment.common.utils;

import com.codesoom.assignment.auth.application.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    public static final String CLAIM_NAME = "userId";
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") final String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode(final Long userId) {
        return Jwts.builder()
                .claim(CLAIM_NAME, userId)
                .signWith(key)
                .compact();
    }

    public Claims decode(final String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException();
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidTokenException();
        }
    }
}
