package com.codesoom.assignment.application.auth;

import com.codesoom.assignment.exceptions.UserNotFoundException;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.exceptions.InvalidPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** 회원 로그인을 담당합니다. */
@Service
public class UserLoginService implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository repository;

    public UserLoginService(PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        User user = repository.findByEmailAndDeletedIsFalse(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없으므로 로그인에 실패했습니다."));

        if (!user.authenticate(loginRequest.getPassword(), passwordEncoder)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않아 로그인에 실패했습니다.");
        }

        return jwtUtil.encode(user.getId());
    }

}
