package com.codesoom.assignment.session.service;

import com.codesoom.assignment.session.exception.InvalidTokenException;
import com.codesoom.assignment.session.exception.LoginFailException;
import com.codesoom.assignment.session.utils.JwtUtil;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private static final String SECRET = "12345678901234567890123456789012";

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long parseToken(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            Claims claims = jwtUtil.decode(token);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException(email));

        if (!user.authenticate(password, passwordEncoder)) {
            throw new LoginFailException(email);
        }

        JwtUtil jwtUtil = new JwtUtil(SECRET);
        return jwtUtil.encode(user.getId());
    }
}
