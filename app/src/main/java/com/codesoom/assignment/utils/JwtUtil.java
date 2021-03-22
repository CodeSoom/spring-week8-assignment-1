package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/** 토큰에 대해 처리한다 */
@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 주어진 이메일과 비밀번호로 토큰을 생성하고 리턴한다.
     *
     * @param email - 토큰 생성을 위한 이메일
     * @return - 주어진 {@code email}를 이용하여 생성 된 토큰
     */
    public String encode(String email) {
        return Jwts.builder()
                .setSubject(email)
                .signWith(key)
                .compact();
    }

    /**
     * 주어진 토큰을 해석하여 사용자 정보를 리턴한다.
     *
     * @param token - 해석하고자 하는 토큰
     * @return 주어진 {@code token}내에 있는 사용자 정보
     */
    public Claims decode(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
