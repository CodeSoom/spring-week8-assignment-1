package com.codesoom.assignment.application;

import com.codesoom.assignment.Fixture;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.DuplicatedEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("UserService 의")
public class UserCommandServiceTest {
    @Autowired
    private UserCommandService userCommandService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("register 메서드는")
    class Describe_register {
        @Nested
        @DisplayName("유저 정보가 주어지면")
        class Context_with_userRegisterData {
            @Test
            @DisplayName("유저 조회 정보를 리턴한다")
            void It_returns_userInquiryInfo() {
                User user = userCommandService.register(Fixture.USER_REGISTER_DATA);

                assertThat(user.getEmail()).isEqualTo(Fixture.USER.getEmail());
                assertThat(user.getRole()).isEqualTo(Fixture.USER.getRole());
            }
        }

        @Nested
        @DisplayName("중복된 이메일이 있다면")
        class Context_with_duplicationEmail {
            @BeforeEach
            void prepare() {
                userCommandService.register(Fixture.USER_REGISTER_DATA);
            }

            @Test
            @DisplayName("예외를 던진다")
            void It_throws_exception() {
                assertThatThrownBy(() -> userCommandService.register(Fixture.USER_REGISTER_DATA))
                        .isExactlyInstanceOf(DuplicatedEmailException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("삭제할 식별자가 주어지면")
        class Context_with_idToDelete {
            private Long idToDelete;

            @BeforeEach
            void prepare() {
                User user = userCommandService.register(Fixture.USER_REGISTER_DATA);
                idToDelete = user.getId();
            }

            @Test
            @DisplayName("유저를 삭제한다")
            void It_delete_user() {
                userCommandService.delete(idToDelete);

                assertThat(userRepository.existsById(idToDelete)).isFalse();
            }
        }
    }
}
