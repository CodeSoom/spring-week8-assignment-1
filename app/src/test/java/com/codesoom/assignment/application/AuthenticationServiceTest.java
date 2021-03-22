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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("AuthenticationService 테스트")
class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    private static final String SECRET = "12345678901234567890123456789010";
    private static final String EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGlzdGVkRW1haWwifQ." +
            "UQodS3elf3Cu4g0PDFHqVloFbcKHHmTTnk0jGmiwPXY";
    private static final String NOT_EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGlzdGVkRW1haWwifQ." +
            "UQodS3elf3Cu4g0PDFHqVloFbcKHHmTTnk0jGmiwPXy";

    private static final String EXISTED_EMAIL = "existedEmail";
    private static final String EXISTED_PASSWORD = "existedPassword";

    private static final String NOT_EXISTED_EMAIL = "existedEmail";
    private static final String NOT_EXISTED_PASSWORD = "existedPassword";

    private static final String USER_EMAIL = EXISTED_EMAIL;
    private static final String OTHER_EMAIL = "otherEmail";
    private static final String ADMIN_EMAIL = "adminEmail";

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        jwtUtil = new JwtUtil(SECRET);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationService(userRepository, roleRepository, jwtUtil, passwordEncoder);
    }

    @Nested
    @DisplayName("authenticateUser 메서드는")
    class Describe_authenticateUser {
        @Nested
        @DisplayName("만약 저장되어 있는 이메일과 비밀번호가 주어진다면")
        class Context_WithExistedEmailAndExistedPassword {
            private final String givenExistedEmail = EXISTED_EMAIL;
            private final String givenExistedPassword = EXISTED_PASSWORD;
            private User user;
            private String encodedPassword;

            @BeforeEach
            void setUp() {
                encodedPassword = passwordEncoder.encode(givenExistedPassword);

                user = User.builder()
                        .email(EXISTED_EMAIL)
                        .password(encodedPassword)
                        .build();
            }

            @Test
            @DisplayName("주어진 이메일과 비밀번호에 해당하는 사용자를 리턴한다")
            void itReturnsUser() {
                given(userRepository.findByEmail(givenExistedEmail)).willReturn(Optional.of(user));

                User authenticatedUser = authenticationService.authenticateUser(givenExistedEmail, givenExistedPassword);

                assertThat(authenticatedUser.getEmail()).isEqualTo(user.getEmail());
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 이메일이 주어진다면")
        class Context_WithNotExistedEmail {
            private final String givenNotExistedEmail = NOT_EXISTED_EMAIL;
            private final String givenExistedPassword = EXISTED_PASSWORD;

            @Test
            @DisplayName("인증 요청이 잘못 되었다는 예외를 던진다")
            void itThrowsAuthenticationBadRequestException() {
                given(userRepository.findByEmail(givenNotExistedEmail)).willReturn(Optional.empty());

                assertThatThrownBy(() -> authenticationService.authenticateUser(givenNotExistedEmail,givenExistedPassword))
                        .isInstanceOf(AuthenticationBadRequestException.class)
                        .hasMessageContaining("Authentication bad request");
            }
        }

        @Nested
        @DisplayName("만약 올바르지 않은 비밀번호가 주어진다면")
        class Context_WithNotExistedPassword {
            private final String givenExistedEmail = EXISTED_EMAIL;
            private final String givenNotExistedPassword = NOT_EXISTED_PASSWORD;
            private User user;
            private String encodedPassword;

            @BeforeEach
            void setUp() {
                encodedPassword = passwordEncoder.encode(givenNotExistedPassword);

                user = User.builder()
                        .email(givenExistedEmail)
                        .password(encodedPassword)
                        .build();
            }

            @Test
            @DisplayName("인증 요청이 잘못 되었다는 예외를 던진다")
            void itThrowsAuthenticationBadRequestException() {
                given(userRepository.findByEmail(givenExistedEmail)).willReturn(Optional.empty());

                assertThatThrownBy(() -> authenticationService.authenticateUser(givenExistedEmail, givenNotExistedPassword))
                        .isInstanceOf(AuthenticationBadRequestException.class)
                        .hasMessageContaining("Authentication bad request");
            }
        }
    }

    @Nested
    @DisplayName("createToken 메서드는")
    class Describe_createToken {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자가 주어진다면")
        class Context_WithExistedUser {
            private SessionCreateData sessionCreateData;
            private SessionResultData sessionResultData;
            private String encodedPassword;
            private User user;

            @BeforeEach
            void setUp() {
                encodedPassword = passwordEncoder.encode(EXISTED_PASSWORD);

                sessionCreateData = SessionCreateData.builder()
                        .email(EXISTED_EMAIL)
                        .password(EXISTED_PASSWORD)
                        .build();

                user = User.builder()
                        .email(EXISTED_EMAIL)
                        .password(encodedPassword)
                        .build();

                sessionResultData = SessionResultData.of(EXISTED_TOKEN);
            }

            @Test
            @DisplayName("주어진 사용자를 이용하여 토큰을 생성하고 해당 토큰을 리턴한다")
            void itCreatesTokenAndReturnsToken() {
                given(userRepository.findByEmail(EXISTED_EMAIL)).willReturn(Optional.of(user));

                SessionResultData token = authenticationService.createToken(sessionCreateData);

                assertThat(token).isEqualTo(sessionResultData);
            }
        }

        @Nested
        @DisplayName("만약 이메일이 저장되어 있지 않은 사용자가 주어진다면")
        class Context_WithUserWithoutEmail {
            private SessionCreateData givenUser;

            @BeforeEach
            void setUp() {
                givenUser = SessionCreateData.builder()
                        .email(NOT_EXISTED_EMAIL)
                        .password(EXISTED_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("인증 요청이 잘못 되었다는 예외를 던진다")
            void itThrowsAuthenticationBadRequestException() {
                given(userRepository.findByEmail(givenUser.getEmail())).willReturn(Optional.empty());

                assertThatThrownBy(() -> authenticationService.createToken(givenUser))
                        .isInstanceOf(AuthenticationBadRequestException.class)
                        .hasMessageContaining("Authentication bad request");
            }
        }

        @Nested
        @DisplayName("만약 비밀번호가 올바르지 않는 사용자가 주어진다면")
        class Context_WithUserWithoutPassword {
            private final String givenNotExistedPassword = NOT_EXISTED_PASSWORD;
            private SessionCreateData givenUser;
            private String encodedPassword;

            @BeforeEach
            void setUp() {
                encodedPassword = passwordEncoder.encode(givenNotExistedPassword);

                givenUser = SessionCreateData.builder()
                        .email(EXISTED_EMAIL)
                        .password(encodedPassword)
                        .build();
            }

            @Test
            @DisplayName("인증 요청이 잘못 되었다는 예외를 던진다")
            void itThrowsAuthenticationBadRequestException() {
                given(userRepository.findByEmail(givenUser.getEmail())).willReturn(Optional.empty());

                assertThatThrownBy(() -> authenticationService.createToken(givenUser))
                        .isInstanceOf(AuthenticationBadRequestException.class)
                        .hasMessageContaining("Authentication bad request");
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_parseToken {
        @Nested
        @DisplayName("만약 유효한 토큰이 주어진다면")
        class Context_WithValidToken {
            private final String givenValidToken = EXISTED_TOKEN;

            @Test
            @DisplayName("주어진 토큰을 해석하여 안에 담긴 내용을 리턴한다")
            void itParsesTokenAndReturnsUser() {
                String email = authenticationService.parseToken(givenValidToken);

                assertThat(email).isEqualTo(EXISTED_EMAIL);
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 토큰이 주어진다면")
        class Context_WithNotValidToken {
            private final String givenNotValidToken = NOT_EXISTED_TOKEN;

            @Test
            @DisplayName("토큰이 유효하지 않다는 예외를 던진다")
            void itThrowsInvalidTokenException() {
                assertThatThrownBy(() -> authenticationService.parseToken(givenNotValidToken))
                        .isInstanceOf(InvalidTokenException.class)
                        .hasMessageContaining("Invalid token");
            }
        }

        @Nested
        @DisplayName("만약 토큰이 주어지지 않는다면")
        class Context_WithoutToken {
            @Test
            @DisplayName("토큰이 유효하지 않다는 예외를 던진다")
            void itThrowsInvalidTokenException() {
                assertThatThrownBy(() -> authenticationService.parseToken(null))
                        .isInstanceOf(InvalidTokenException.class)
                        .hasMessageContaining("Invalid token");
            }
        }
    }

    @Nested
    @DisplayName("roles 메서드는")
    class Describe_roles {
        @Nested
        @DisplayName("만약 사용자 이메일이 주어진다면")
        class Context_WithExistedUserEmail {
            private final String givenExistedEmail = USER_EMAIL;

            @Test
            @DisplayName("사용자 계정을 리턴한다")
            void itReturnsUser() {
                given(roleRepository.findAllByEmail(givenExistedEmail))
                        .willReturn(List.of(new Role("USER")));

                assertThat(
                        authenticationService.roles(givenExistedEmail).stream()
                                .map(Role::getName)
                                .collect(Collectors.toList())
                ).isEqualTo(List.of("USER"));
            }
        }

        @Nested
        @DisplayName("만약 admin 이메일이 주어진다면")
        class Context_WithExistedAdminEmail {
            private final String givenExistedEmail = ADMIN_EMAIL;

            @Test
            @DisplayName("사용자 계정과 admin 계정을 리턴한다")
            void itReturnsUserAndAdmin() {
                given(roleRepository.findAllByEmail(givenExistedEmail))
                        .willReturn(Arrays.asList(new Role("USER"), new Role("ADMIN")));

                assertThat(
                        authenticationService.roles(givenExistedEmail).stream()
                                .map(Role::getName)
                                .collect(Collectors.toList())
                ).isEqualTo(Arrays.asList("USER", "ADMIN"));
            }
        }
    }
}
