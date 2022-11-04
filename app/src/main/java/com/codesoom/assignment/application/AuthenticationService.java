package com.codesoom.assignment.application;

import com.codesoom.assignment.security.UserAuthentication;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

/**
 * 인증을 관리하는 서비스
 */
@Service
public class AuthenticationService {
    private final RoleService roleService;
    private final JwtUtil jwtUtil;

    public AuthenticationService(RoleService roleService, JwtUtil jwtUtil) {
        this.roleService = roleService;
        this.jwtUtil = jwtUtil;
    }

    public UserAuthentication authenticate(String accessToken) {
        Long userId = getUserId(accessToken);
        return new UserAuthentication(userId, roleService.getRoles(userId));
    }

    public Long getUserId(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
