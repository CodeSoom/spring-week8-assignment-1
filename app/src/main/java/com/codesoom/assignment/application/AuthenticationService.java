package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 인증 관련 비즈니스 로직을 담당한다.
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
     * 주어진 이메일과 비밀번호로 로그인하고 생성한 액세스 토큰을 리턴한다.
     *
     * @param email    인자
     * @param password 인자
     * @return 생성한 액세스 토큰
     * @throws LoginFailException 로그인에 실패한 경우
     */
    public String login(String email, String password)
            throws LoginFailException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException(email));

        if (!user.authenticate(password, passwordEncoder)) {
            throw new LoginFailException(email);
        }

        return jwtUtil.encode(user.getId());
    }

    /**
     * 주어진 액세스 토큰을 파싱해 파싱한 값을 반환한다.
     *
     * @param accessToken 액세스 토큰
     * @return 파싱한 값
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우
     */
    public Long parseToken(String accessToken)
            throws InvalidTokenException {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }

    /**
     * 주어진 회원 id에 해당하는 모든 권한을 반환한다.
     *
     * @param userId 회원 식별자
     * @return 모든 권한
     */
    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
