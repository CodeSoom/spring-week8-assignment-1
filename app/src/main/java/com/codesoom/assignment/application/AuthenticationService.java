package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * JWT 토큰을 반환한다.
     *
     * @param email 사용자 이메일
     * @param password 사용자 패스워드
     * @throws UserNotFoundException 로그인 정보에 해당하는 사용자가 존재하지 않을 경우
     * @throws LoginFailException 사용자의 패스워드 정보가 일치하지 않은 경우
     * @return JWT 반환
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
     * JWT를 검증한다.
     *
     * @param accessToken JWT
     * @throws InvalidTokenException 토큰 정보가 null 또는 사이즈가 0이거나 첫 글자가 공백 , 유효하지 않은 토큰이라면 예외를 던진다.
     * @throws UserNotFoundException 페이로드에 담긴 식별자에 해당하는 사용자가 없는 경우
     * @return userId JWT의 페이로드에 담겨있는 사용자 식별자를 반환한다.
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }

    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
