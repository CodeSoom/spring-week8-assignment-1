package com.codesoom.assignment.session.application;

import com.codesoom.assignment.common.utils.JwtUtil;
import com.codesoom.assignment.role.domain.Role;
import com.codesoom.assignment.role.domain.RoleRepository;
import com.codesoom.assignment.session.application.port.AuthenticationUseCase;
import com.codesoom.assignment.session.exception.LoginFailException;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements AuthenticationUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(final UserRepository userRepository,
                                 final RoleRepository roleRepository,
                                 final JwtUtil jwtUtil,
                                 final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(final String email, final String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(LoginFailException::new);

        if (!user.authenticate(password, passwordEncoder)) {
            throw new LoginFailException();
        }

        return jwtUtil.encode(user.getId());
    }

    public Long parseToken(final String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }

    public List<Role> roles(final Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
