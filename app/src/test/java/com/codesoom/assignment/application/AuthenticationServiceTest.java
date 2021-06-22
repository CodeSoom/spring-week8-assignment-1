package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private final String SECRET = "12345678901234567890123456789010";

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private final String INVALID_TOKEN = VALID_TOKEN + "INVALID";

    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);
    private RoleRepository roleRepository = mock(RoleRepository.class);

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        authenticationService = new AuthenticationService(
                userRepository, roleRepository, jwtUtil, passwordEncoder);

        User user = User.builder().id(1L).build();
        user.changePassword("test", passwordEncoder);

        given(userRepository.findByEmail("tester@example.com"))
                .willReturn(Optional.of(user));

        given(roleRepository.findAllByUserId(1L))
                .willReturn(Arrays.asList(new Role("USER")));
        given(roleRepository.findAllByUserId(1004L))
                .willReturn(Arrays.asList(new Role("USER"), new Role("ADMIN")));
    }

    @Nested
    @DisplayName("login 메서드")
    class DescribeLogin {
        @Nested
        @DisplayName("올바른 이메일과 비밀번호 입력 시")
        class ContextLoginWithRightEmailAndPassword {
            @Test
            @DisplayName("회원 식별자에 따른 인증토큰을 반환한다")
            void returnsValidToken() {
                String accessToken = authenticationService.login(
                        "tester@example.com", "test");

                assertThat(accessToken).isEqualTo(VALID_TOKEN);

                verify(userRepository).findByEmail("tester@example.com");
            }
        }

        @Nested
        @DisplayName("올바른 이메일과 잘못된 비밀번호 입력 시")
        class ContextLoginWithRightEmailAndWrongPassword {
            @Test
            @DisplayName("로그인 실패 예외를 반환한다")
            void throwsLoginFailException() {
                assertThatThrownBy(
                        () -> authenticationService.login("tester@example.com", "xxx")
                ).isInstanceOf(LoginFailException.class);

                verify(userRepository).findByEmail("tester@example.com");
            }
        }

        @Nested
        @DisplayName("잘못된 이메일 입력 시")
        class ContextLoginWithWrongEmail {
            @Test
            @DisplayName("로그인 실패 예외를 반환한다")
            void throwsLoginFailException() {
                assertThatThrownBy(
                        () -> authenticationService.login("badguy@example.com", "test")
                ).isInstanceOf(LoginFailException.class);

                verify(userRepository).findByEmail("badguy@example.com");
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드")
    class DescribeParseToken {
        @Nested
        @DisplayName("유효한 토큰이라면")
        class ContextWithValidToken {
            @Test
            @DisplayName("유효한 토큰이라면")
            void returnsUserId() {
                Long userId = authenticationService.parseToken(VALID_TOKEN);

                assertThat(userId).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("잘못된 토큰이라면")
        class ContextWithInvalidToken {
            @Test
            @DisplayName("잘못된 토큰 예외를 반환한다")
            void throwsInvalidTokenException() {
                assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("빈 토큰이라면")
        class ContextWithEmptyToken {
            @ParameterizedTest(name = "{index}: <{0}>")
            @NullSource
            @EmptySource
            @ValueSource(strings = {" "})
            @DisplayName("잘못된 토큰 예외를 반환한다")
            void throwsInvalidTokenException(String input) {
                assertThatThrownBy(() -> authenticationService.parseToken(input))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }

    @Test
    @DisplayName("roles 메서드")
    void roles() {
        assertThat(
                authenticationService.roles(1L).stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        ).isEqualTo(Arrays.asList("USER"));

        assertThat(
                authenticationService.roles(1004L).stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        ).isEqualTo(Arrays.asList("USER", "ADMIN"));
    }
}
