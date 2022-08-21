package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 유효한 로그인이라면 토큰을 생성하고 리턴한다.
     *
     * @param data 로그인 정보
     * @return 토큰 정보
     */
    public SessionResponseData login(SessionRequestData data) {
        String email = data.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException(email));

        if (!user.isMatchPassword(data.getPassword())) {
            throw new LoginFailException(email);
        }

        return new SessionResponseData(jwtUtil.encode(user.getId(), user.getRole()));
    }

    public Claims parseToken(String accessToken) {
        return jwtUtil.decode(accessToken);
    }
}
