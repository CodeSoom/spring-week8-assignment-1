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
 * 인증, 인가 처리한다.
 * @author kyuwon aka (kyuwoon369@gmail.com)
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
     * 주어진 이메일의 사용자가 없거나 비밀번호가 일치하지 않으면 로그인이 실패했다는 예외를 던진다.
     * 주어진 이메일에 일치하는 사용자의 비밀번호를 확인하고 일치하면 인코딩된 아이디를 리턴한다.
     * @param email 로그인시 입력한 이메일
     * @param password 로그인 시 입력한 비밀번호
     * @return 암호화된 아이디
     * @throws LoginFailException 일치하는 이메일이 없거나 비밀번호가 맞지 않는 경우
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
     * 인증된 토큰이 들어오면 디코딩된 아이디를 리턴한다.
     * @param accessToken 인증된 토큰
     * @return 복호화된 사용자 아이디
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }

    /**
     * 주어진 id의 사용자 권한 목록을 리턴한다.
     * @param userId 사용자 아이디
     * @return 사용자 권한 목록
     */
    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
