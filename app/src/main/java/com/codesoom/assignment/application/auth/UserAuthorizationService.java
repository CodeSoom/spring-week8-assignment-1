package com.codesoom.assignment.application.auth;

import com.codesoom.assignment.domain.users.Role;
import com.codesoom.assignment.domain.users.RoleRepository;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthorizationService implements AuthorizationService {

    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    public UserAuthorizationService(RoleRepository roleRepository, JwtUtil jwtUtil) {
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Long parseToken(String accessToken) {
        final Claims claims = jwtUtil.decode(accessToken);
        final Long userId = claims.get("userId", Long.class);

        return userId;
    }

    @Override
    public List<Role> roles(Long id) {
        return roleRepository.findAllByUserId(id);
    }

}
