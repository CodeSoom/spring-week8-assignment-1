package com.codesoom.assignment.auth.application;

import com.codesoom.assignment.auth.infra.JwtUtil;
import com.codesoom.assignment.user.domain.Role;
import com.codesoom.assignment.user.domain.RoleRepository;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    private static final Long GIVEN_USER_ID = 1L;
    private static final String GIVEN_USER_EMAIL = "tester@example.com";
    private static final String GIVEN_USER_PASSWORD = "test";
    private static final String WRONG_EMAIL = GIVEN_USER_EMAIL + "_WRONG";
    private static final String WRONG_PASSWORD = GIVEN_USER_PASSWORD + "_WRONG";
    private static final String ENCODED_PASSWORD = "$2a$10$y9ia8/dUMn1sZz/Qyc/SWuMp0TjaJdoHxnK1lVj3OMkA2WT/31YTq";

    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);
    private RoleRepository roleRepository = mock(RoleRepository.class);
    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        authenticationService = new AuthenticationService(
                userRepository, roleRepository, jwtUtil, passwordEncoder);
    }

    @Nested
    @DisplayName("authenticate 메서드는")
    class Describe_authenticate {

        @Nested
        @DisplayName("등록된 이메일과 비밀번호가 주어지면")
        class Context_with_exist_email_password {
            final String email = GIVEN_USER_EMAIL;
            final String password = GIVEN_USER_PASSWORD;
            final User user = User.builder()
                    .id(1L)
                    .password(ENCODED_PASSWORD)
                    .deleted(false)
                    .build();

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(GIVEN_USER_EMAIL))
                        .willReturn(Optional.of(user));
                given(passwordEncoder.matches(password, ENCODED_PASSWORD))
                        .willReturn(true);
            }

            @DisplayName("인증 토큰을 리턴한다.")
            @Test
            void it_returns_token() {
                String token = authenticationService.login(email, password);
                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이메일이 주어지면")
        class Context_with_not_wrong_email {
            final String email = WRONG_EMAIL;
            final String password = GIVEN_USER_PASSWORD;

            @DisplayName("로그인 실패 예외를 던진다.")
            @Test
            void It_throws_exception() {
                assertThrows(LoginFailException.class,
                        () -> authenticationService.login(email, password));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 비밀번호가 주어지면")
        class Context_with_wrong_password {
            final String email = GIVEN_USER_EMAIL;
            final String password = WRONG_PASSWORD;
            final User user = User.builder()
                    .id(1L)
                    .build();

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(GIVEN_USER_EMAIL))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("로그인 실패 예외를 던진다.")
            @Test
            void It_throws_exception() {
                assertThrows(LoginFailException.class,
                        () -> authenticationService.login(email, password));
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_parseToken {

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {
            final String token = VALID_TOKEN;

            @BeforeEach
            void setUp() {
                final User user = User.builder()
                        .id(GIVEN_USER_ID)
                        .build();
                given(userRepository.findById(GIVEN_USER_ID))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("사용자 식별자를 리턴한다.")
            @Test
            void it_returns_user_id() {
                final Long userId = authenticationService.parseToken(token);

                User actual = userRepository.findById(userId).get();
                assertThat(actual.getId()).isEqualTo(GIVEN_USER_ID);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_invalid_token {
            final String token = INVALID_TOKEN;

            @DisplayName("로그인 실패 예외를 던진다.")
            @Test
            void it_returns_user_id() {
                assertThrows(InvalidTokenException.class,
                        () -> authenticationService.parseToken(token));
            }
        }
    }

    @Nested
    @DisplayName("roles 메서드는")
    class Describe_roles {
        @Nested
        @DisplayName("사용자 권한이 등록되어 있으면")
        class Context_with_user_roles {

            @BeforeEach
            void setUp() {
                given(roleRepository.findAllByUserId(GIVEN_USER_ID))
                        .willReturn(
                                List.of(new Role("USER"), new Role("ADMIN"))
                        );
            }

            @DisplayName("권한 목록을 리턴한다.")
            @Test
            void it_returns_roles() {
                List<Role> roles = authenticationService.roles(GIVEN_USER_ID);

                assertThat(roles).extracting("name").contains("USER", "ADMIN");
            }
        }
    }
}
