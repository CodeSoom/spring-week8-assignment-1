package com.codesoom.assignment.auth.application;

import com.codesoom.assignment.auth.infra.JwtUtil;
import com.codesoom.assignment.user.domain.Role;
import com.codesoom.assignment.user.domain.RoleRepository;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 사용자 인증을 처리한다.
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
     * 올바른 사용자 정보라면 인증 토큰을 리턴하고, 그렇지 않으면 예외를 던집니다.
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @throws LoginFailException 로그인에 실패했을 경우
     * @return 인증 토큰
     */
    public String login(String email, String password) throws LoginFailException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException(email));

        if (!user.authenticate(password, passwordEncoder)) {
            throw new LoginFailException(email);
        }

        return jwtUtil.encode(user.getId());
    }

    /**
     * 토큰을 파싱하여 사용자 아이디를 리턴한다.
     * @param accessToken 인증 토큰
     * @return 사용자 id
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }

    /**
     * 주어진 사용자 id에 해당하는 권한 목록을 리턴한다.
     * @param userId 주어진 사용자 id
     * @return 사용자 권한 목록
     */
    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
