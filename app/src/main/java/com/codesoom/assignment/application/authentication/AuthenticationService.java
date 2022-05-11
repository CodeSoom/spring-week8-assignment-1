package com.codesoom.assignment.application.authentication;

import com.codesoom.assignment.UserNotFoundException;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.exceptions.InvalidTokenException;
import com.codesoom.assignment.exceptions.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationService(
            UserRepository userRepository,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "Email[" + email + "] 에 해당하는 사용자를 찾을 수 없어 로그인을 할 수 없습니다."
                        )
                );
        if (!user.getPassword().equals(password)) {
            throw new LoginFailException(email);
        }
        return jwtUtil.encode(user.getId());
    }

    /**
     * 생성한 토큰에서 claims에 있는 id 파싱
     * @param accessToken Claim에 id가 담겨있는 토큰
     * @return 토큰에 포함된 id
     */
    public Long parseToken(String accessToken) throws InvalidTokenException {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("id", Long.class);
    }

}
