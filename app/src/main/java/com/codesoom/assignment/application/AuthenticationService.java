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
 * Authentication(인증) 관련 비즈니스 로직
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
     * 로그인 정보로 인증 후 액세스 토큰을 반환한다.
     * @param email 로그인 이메일
     * @param password 로그인 비밀번호
     * @return 액세스 토큰
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
     * 액세스 토큰을 받아서 디코딩 후 사용자 ID를 리턴한다.
     * @param accessToken 액세스 토큰
     * @return 사용자 ID
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }

    /**
     * 사용자 ID의 사용자가 가지고 있는 사용자 권한을 리턴한다.
     * @param userId 사용자 ID
     * @return 사용자 권한 리스트
     */
    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
