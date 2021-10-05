package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 인증 서비스를 제공한다.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 JwtUtil jwtUtil,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 로그인 정보를 확인 후 인증 토큰을 반환한다.
     *
     * @param email 이메일 정보
     * @param password 비밀 번호
     * @return 인증 토큰
     * @throws LoginFailException 이메일로 회원을 찾을 수 없거나 비밀번호가 일치하지 않을 경우 예외를 던진다.
     */
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException(email));

        if (!user.authenticate(password, passwordEncoder)) {
            throw new LoginFailException(email);
        }

        return jwtUtil.encode(user.getId());
    }

    /**
     * 인증 토큰을 분석해 식별자 정보를 반환한다.
     *
     * @param accessToken 인증 토큰
     * @return 식별자 정보
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }

    /**
     * 해당 식별자의 권한을 확인한다.
     *
     * @param userId 식별자
     * @return 권한 목록
     */
    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
