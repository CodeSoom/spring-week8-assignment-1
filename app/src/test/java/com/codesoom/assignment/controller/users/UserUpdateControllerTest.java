package com.codesoom.assignment.controller.users;

import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.exceptions.UserNotFoundException;
import com.codesoom.assignment.application.users.UserUpdateService;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.domain.users.UserResponseDto;
import com.codesoom.assignment.domain.users.UserSaveDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserUpdateController 클래스")
@ActiveProfiles("test")
@SpringBootTest
public class UserUpdateControllerTest {

    private static final String EMAIL = "hgd@gmail.com";
    private static final String PASSWORD = "hgdZzang123";

    private UserUpdateController controller;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserUpdateService service;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() {
        this.controller = new UserUpdateController(service);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    private User saveUser() {
        User user = User.of("김철수", EMAIL);
        user.changePassword(PASSWORD, passwordEncoder);

        return repository.save(user);
    }

    @DisplayName("updateUser 메서드는")
    @Nested
    class Describe_update_user {

        @DisplayName("찾을 수 있는 회원의 id가 주어지면")
        @Nested
        class Context_with_exist_user {

            private Long EXIST_ID;
            private final UserSaveDto UPDATE_TO_USER
                    = new UserSaveDto("심청이", "hyonyeoChung@gmail.com", "tlacjd123");

            @BeforeEach
            void setup() {
                this.EXIST_ID = saveUser().getId();
            }

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("회원 정보를 수정한 뒤, 수정 결과를 반환한다.")
            @Test
            void it_returns_updated_user() {
                UserResponseDto userResponseDto = controller.updateUser(EXIST_ID, UPDATE_TO_USER);

                assertThat(userResponseDto.getId()).isEqualTo(EXIST_ID);
                assertThat(userResponseDto.getName()).isEqualTo(UPDATE_TO_USER.getName());
                assertThat(userResponseDto.getEmail()).isEqualTo(UPDATE_TO_USER.getEmail());
                assertThat(passwordEncoder
                        .matches(UPDATE_TO_USER.getPassword(), userResponseDto.getPassword())).isTrue();
            }
        }

        @DisplayName("찾을 수 없는 회원의 id가 주어지면")
        @Nested
        class Context_with_not_exist_user {

            private final Long NOT_EXIST_ID = 999L;
            private final UserSaveDto UPDATE_TO_USER
                    = new UserSaveDto("심청이", "hyonyeoChung@gmail.com", "tlacjd123");

            @BeforeEach
            void setup() {
                if (repository.existsById(NOT_EXIST_ID)) {
                    repository.deleteById(NOT_EXIST_ID);
                }
            }

            @DisplayName("예외를 던진다.")
            @Test
            void it_throws_exception() {
                assertThatThrownBy(() -> controller.updateUser(NOT_EXIST_ID, UPDATE_TO_USER))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

}
