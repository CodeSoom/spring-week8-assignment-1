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
 * Service for Authentication
 *
 * @author sim
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
     * 이메일과 패스워드를 통해 로그인 한다.
     *
     * @param email - 요청 이메일
     * @param password - 평문 형태의 비밀번호
     * @throws LoginFailException - 유효한 이메일이 없거나, 비밀번호가 일치하지 않을 때 발생.
     * @return JWT 토큰.
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
     * AccessToken에서 userId를 추출한다.
     *
     * @param accessToken - JWT AccessToken
     * @return JWT AccessToken에서 추출한 userID.
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }

    /**
     * userId에 대한 권한 목록을 조회한다.
     *
     * @param userId - 유저 고유 식별 id
     * @return 유저에 대한 권한 리스트.
     */
    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
