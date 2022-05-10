package com.codesoom.assignment.application.auth;

import com.codesoom.assignment.application.ServiceTest;
import com.codesoom.assignment.exceptions.UserNotFoundException;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.exceptions.InvalidPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest extends ServiceTest {

    private static final String USER_EMAIL = "hgd@codesoom.com";
    private static final String USER_PASSWORD = "hgdZzang123!";

    private UserLoginService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() {
        this.service = new UserLoginService(passwordEncoder, jwtUtil, repository);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    private User saveUser() {
        User user = User.of("김철수", USER_EMAIL);
        user.changePassword(USER_PASSWORD, passwordEncoder);

        return repository.save(user);
    }

    @DisplayName("login 메서드는")
    @Nested
    class Describe_login {

        @DisplayName("찾을 수 있는 이메일과")
        @Nested
        class Context_with_exist_user {

            @DisplayName("정확한 비밀번호가 주어지면")
            @Nested
            class Context_with_correct_password {

                private final LoginRequest LOGIN_REQUEST_DTO = new LoginRequest() {
                    @Override
                    public String getEmail() {
                        return USER_EMAIL;
                    }

                    @Override
                    public String getPassword() {
                        return USER_PASSWORD;
                    }
                };

                @BeforeEach
                void setup() {
                    saveUser();
                }

                @AfterEach
                void cleanup() {
                    repository.deleteAll();
                }

                @DisplayName("성공적으로 토큰을 발급한다.")
                @Test
                void it_return_token() {
                    String access_token = service.login(LOGIN_REQUEST_DTO);

                    assertThat(access_token).contains(".");
                }
            }


            @DisplayName("틀린 비밀번호가 주어지면")
            @Nested
            class Context_with_incorrect_password {
                private final String INCORRECT_PASSWORD = "HiImTroll";

                private final LoginRequest INCORRECT_PASSWORD_REQUEST = new LoginRequest() {
                    @Override
                    public String getEmail() {
                        return USER_EMAIL;
                    }

                    @Override
                    public String getPassword() {
                        return INCORRECT_PASSWORD;
                    }
                };

                @BeforeEach
                void setup() {
                    saveUser();
                }

                @AfterEach
                void cleanup() {
                    repository.deleteAll();
                }

                @DisplayName("예외를 던진다.")
                @Test
                void it_throws_invalid_password() {
                    assertThatThrownBy(() -> service.login(INCORRECT_PASSWORD_REQUEST))
                            .isInstanceOf(InvalidPasswordException.class);
                }
            }
        }

        @DisplayName("찾을 수 없는 회원 로그인 정보가 주어지면")
        @Nested
        class Context_with_not_exist_user {

            private final String NOT_EXIST_USER_EMAIL = "fail" + USER_EMAIL;
            private final LoginRequest NOT_EXIST_LOGIN_REQUEST = new LoginRequest() {
                @Override
                public String getEmail() {
                    return NOT_EXIST_USER_EMAIL;
                }

                @Override
                public String getPassword() {
                    return USER_PASSWORD;
                }
            };

            @BeforeEach
            void setup() {
                User user = repository.findByEmailAndDeletedIsFalse(NOT_EXIST_USER_EMAIL).orElse(null);
                if (user != null) {
                    repository.delete(user);
                }
            }

            @DisplayName("예외를 던진다.")
            @Test
            void it_throws_user_not_found() {
                assertThatThrownBy(() -> service.login(NOT_EXIST_LOGIN_REQUEST))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

}
