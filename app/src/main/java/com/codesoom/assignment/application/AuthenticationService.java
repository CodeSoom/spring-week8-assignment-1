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
 * The {@code AuthenticationService} class represents the Authentication service.
 * The class {@code AuthenticationService} includes methods for
 * encoding and decoding JSON Web Token to authenticate users and getting roles by the user's id.
 *
 * @see org.springframework.stereotype.Service
 */
@Service
public class AuthenticationService {

    /**
     * Repository for User Domain.
     */
    private final UserRepository userRepository;

    /**
     * Repository for Role Domain.
     */
    private final RoleRepository roleRepository;

    /**
     * The {@code JwtUtil} class is a utility class that encodes and decodes JSON Web Token.
     */
    private final JwtUtil jwtUtil;

    /**
     * Service interface for encoding passwords. It implements BCryptPasswordEncoder.
     *
     * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code AuthenticationService} by userRepository, roleRepository, jwtUtil and the passwordEncoder.
     *
     * @param userRepository
     *        The repository of the user
     *
     * @param roleRepository
     *        The repository of the role
     *
     * @param jwtUtil
     *        The utility class about Json Web Token
     *
     * @param passwordEncoder
     *        The Bean of PasswordEncoder
     */
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
     * Returns jwt of the user's id.
     *
     * @param email
     *        The email address of the user
     *
     * @param password
     *        The password of the user
     *
     * @return the jwt token created through by the user's id
     *
     * @throws LoginFailException if you can't find the user by email or the password doesn't match.
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
     * Returns user's id by decoded jwt.
     *
     * @param accessToken
     *        The jwt by the user's id
     *
     * @return the user's id through by decoded jwt
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }

    /**
     * Returns List of roles by user's id.
     *
     * @param userId
     *        The id of the user
     *
     * @return The list of roles by user's id
     */
    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
