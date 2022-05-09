package com.codesoom.assignment.application.users;

import com.codesoom.assignment.application.ServiceTest;
import com.codesoom.assignment.exceptions.UserNotFoundException;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserDeleteServiceTest extends ServiceTest {
    
    private UserCommandService service;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        this.service = new UserCommandService(repository, passwordEncoder);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
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
                User user = User.of("홍길동", "hgd@codesoom.com");
                user.changePassword( "hgd123!", passwordEncoder);
                this.EXIST_ID = repository
                        .save(user)
                        .getId();
            }

            @DisplayName("회원의 삭제 상태를 true로 변경한다.")
            @Test
            void it_will_delete_user() {
                User user = service.deleteUser(EXIST_ID);

                assertThat(user.isDeleted()).isTrue();
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
                assertThatThrownBy(() -> service.deleteUser(NOT_EXIST_ID))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }
    
}
