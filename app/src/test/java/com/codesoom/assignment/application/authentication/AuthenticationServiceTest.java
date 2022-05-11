package com.codesoom.assignment.application.authentication;

import com.codesoom.assignment.UserNotFoundException;
import com.codesoom.assignment.domain.TestUserRepositoryDouble;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.exceptions.InvalidTokenException;
import com.codesoom.assignment.exceptions.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.security.auth.message.AuthException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("AuthenticationService 에서")
class AuthenticationServiceTest {
    private final static String USERNAME = "username1";
    private final static String EMAIL = "auth@auth.com";
    private final static String PASSWORD = "password";

    @Autowired
    private TestUserRepositoryDouble userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationService authenticationService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    /**
     * 하나의 User를 생성해 등록합니다
     * @return 생성한 User
     */
    private User createUser() {
        User user = User.of(
                USERNAME,
                EMAIL,
                PASSWORD
        );
        return userRepository.save(user);
    }


    @Nested
    @DisplayName("login 메소드는")
    class Describe_of_login {

        @Nested
        @DisplayName("로그인할 수 있는 사용자의 이메일과 패스워드가 주어지면")
        class Context_with_valid_email_and_password {
            private String loginEmail;
            private String password;

            @BeforeEach
            void setUp() {
                User user = createUser();
                loginEmail = user.getEmail();
                password = user.getPassword();
            }

            @Test
            @DisplayName("이메일에 맞는 사용자를 찾아서 사용자의 id로 claim을 등록한 jwt 토큰을 리턴한다")
            void it_return_jwt_token() {
                String jwtToken = authenticationService.login(loginEmail, password);

                assertThat(jwtToken).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("로그인할 수 없는 사용자의 이메일이 주어지면")
        class Context_with_invalid_email {
            private String loginEmail;
            private String password;

            @BeforeEach
            void setUp() {
                User user = createUser();
                loginEmail = user.getEmail();
                password = user.getPassword();

                userRepository.delete(user);
            }

            @Test
            @DisplayName("UserNotFoundException을 던진다")
            void it_throw_userNotFoundException() {
                assertThatThrownBy(() -> authenticationService.login(loginEmail, password))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("로그인할 수 없는 사용자의 패스워드가 주어지면")
        class Context_iwht_invalid_password {
            private String loginEmail;
            private String password;

            @BeforeEach
            void setUp() {
                User user = createUser();
                loginEmail = user.getEmail();
                password = user.getPassword() + "WrongPassword";
            }

            @Test
            @DisplayName("LoginFailException을 던진다")
            void it_throw_loginFailException() {
                assertThatThrownBy(() -> authenticationService.login(loginEmail, password))
                        .isInstanceOf(LoginFailException.class);
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메소드는")
    class Describe_of_parseToken {

        @Nested
        @DisplayName("유효한 accessToken이 주어지면")
        class Context_with_valid_accessToken {
            private String accessToken;
            private final User user = createUser();

            @BeforeEach
            void setUp() {
                accessToken = jwtUtil.encode(user.getId());
            }

            @Test
            @DisplayName("accessToken 에서 id를 가져올 수 있다")
            void it_return_id() throws AuthException {
                Long id = authenticationService.parseToken(accessToken);

                assertThat(id).isEqualTo(user.getId());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 accessToken이 주어지면")
        class Context_with_invalid_accessToken {
            private String accessToken;
            private final User user = createUser();

            @BeforeEach
            void setUp() {
                accessToken = jwtUtil.encode(user.getId());
                accessToken = accessToken + "InvalidToken";
            }

            @Test
            @DisplayName("InvalidTokenException을 던진다")
            void it_throw_invalidTokenException() {
                assertThatThrownBy(() -> authenticationService.parseToken(accessToken))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}