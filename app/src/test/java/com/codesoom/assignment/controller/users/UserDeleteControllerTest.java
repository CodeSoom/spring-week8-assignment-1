package com.codesoom.assignment.controller.users;

import com.codesoom.assignment.application.users.UserDeleteService;
import com.codesoom.assignment.exceptions.UserNotFoundException;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
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

@DisplayName("UserDeleteController 클래스")
@ActiveProfiles("test")
@SpringBootTest
public class UserDeleteControllerTest {

    private static final String EMAIL = "hgd@codesoom.com";
    private static final String PASSWORD = "hgdZzang123!";

    private UserDeleteController controller;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDeleteService service;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() {
        this.controller = new UserDeleteController(service);
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

    @DisplayName("deleteUser 메서드는")
    @Nested
    class Describe_delete_user {

        @DisplayName("찾을 수 있는 회원의 id가 주어지면")
        @Nested
        class Context_with_exist_user {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = saveUser().getId();
            }

            @DisplayName("성공적으로 회원을 삭제한다.")
            @Test
            void it_will_delete_user() {
                controller.deleteUser(EXIST_ID);

                assertThat(repository.findById(EXIST_ID).get().isDeleted()).isTrue();
            }
        }

        @DisplayName("찾을 수 없는 회원의 id가 주어지면")
        @Nested
        class Context_with_not_exist_user {

            private final Long NOT_EXIST_ID = 999L;

            @BeforeEach
            void setup() {
                if (repository.existsById(NOT_EXIST_ID)) {
                    repository.deleteById(NOT_EXIST_ID);
                }
            }

            @DisplayName("예외를 던진다.")
            @Test
            void it_throws_exception() {
                assertThatThrownBy(() -> controller.deleteUser(NOT_EXIST_ID))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

}
