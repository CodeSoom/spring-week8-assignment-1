package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionCreateData;
import com.codesoom.assignment.dto.SessionResultData;
import com.codesoom.assignment.errors.AuthenticationBadRequestException;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/** 토큰에 대한 요청을 수행한다. */
@Service
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 JwtUtil jwtUtil,
                                 PasswordEncoder passwordEncoder
                                 ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 주어진 사용자를 인증하고 사용자의 이메일을 이용해 토큰을 생성하여 리턴한다.
     *
     * @param sessionCreateData - 인증하고자 하는 사용자
     * @return 생성된 토큰
     */
    public SessionResultData createToken(SessionCreateData sessionCreateData) {
        User user = authenticateUser(
                sessionCreateData.getEmail(),
                sessionCreateData.getPassword()
        );

        String accessToken = jwtUtil.encode(user.getEmail());

        return SessionResultData.of(accessToken);
    }

    /**
     * 주어진 토큰을 해석하여 사용자 정보를 리턴한다.
     *
     * @param token - 해석하고자 하는 토큰
     * @return 주어진 {@code token} 안에 있는 사용자 정보
     * @throws InvalidTokenException 만약
     *         주어진 {@code token}이 비어있는 경우, 공백인 경우, 유효하지 않을 경우
     */
    public String parseToken(String token) {
        if(token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            return jwtUtil.decode(token).getSubject();
        } catch(SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }

    /**
     * 주어진 이메일과 비밀번호에 해당하는 사용자를 리턴한다.
     *
     * @param email - 조회하고자 하는 사용자 이메일
     * @param password - 조회하고자 하는 사용자 비밀번호
     * @return 주어진 {@code email}, {@code password}에 해당하는 사용자
     * @throws AuthenticationBadRequestException 만약
     *         {@code email}에 해당되는 사용자가 저장되어 있지 않은 경우
     *         {@code email}에 해당하는 사용자가 저장되어 있지만 {@code password}이 다른 경우
     *         {@code email}에 해당하는 사용자가 저장되어 있지만 이미 삭제된 경우
     */
    public User authenticateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.authenticate(password, passwordEncoder))
                .orElseThrow(AuthenticationBadRequestException::new);
    }

    /**
     * 주어진 이메일에 해당하는 계정을 리턴한다.
     *
     * @param email - 계정을 확인하고자 하는 이메일
     * @return 주어진 {@code email}에 해당하는 계정
     */
    public List<Role> roles(String email) {
        return roleRepository.findAllByEmail(email);
    }
}
