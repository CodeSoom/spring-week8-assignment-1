package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 인증 관련 비즈니스 로직을 담당합니다.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthenticationService(UserRepository userRepository,
                                 JwtUtil jwtUtil,
                                 PasswordEncoder passwordEncoder,
                                 RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * 정상적인 로그인 정보가 주어진다면 인증 토큰을 발급합니다.
     *
     * @param userLoginData 로그인 정보, 사용자 이메일과 비밀번호
     * @return 인증 토큰
     * @throws  LoginFailException 유효하지 않은 이메일이 주어졌을 경우
     */
    public String login(SessionRequestData userLoginData) {
        User user = userRepository.findByEmail(userLoginData.getEmail())
                .orElseThrow(() -> new LoginFailException(userLoginData.getEmail()));

        if (!user.authenticate(userLoginData.getPassword())) {
            throw new LoginFailException(userLoginData.getEmail());
        }

        return jwtUtil.encode(user.getId());
    }

    /**
     * 주어진 인증 토큰을 복호화하여 사용자 id를 반환합니다.
     *
     * @param accessToken 인증 토큰
     * @return 사용자 id
     * @throws InvalidTokenException 유효하지 않은 인증 토큰이 주어졌을 경우
     */
    public Long parseToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new InvalidTokenException(accessToken);
        }
        try {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidTokenException(accessToken);
        }
    }

    /**
     * 주어진 사용자 id에 해당하는 권한 목록을 반환합니다.
     *
     * @param userId 해당 사용자의 식별자
     * @return 해당 사용자의 권한 목록
     */
    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
