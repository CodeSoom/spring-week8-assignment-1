package com.codesoom.assignment.domain.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserTest {

    private PasswordEncoder passwordEncoder;

    private static final Long ID = 1L;
    private static final String NAME = "홍길동";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @BeforeEach
    void setup() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void createNoArgsConstructor () {
        assertThat(new User()).isNotNull();
    }

    @DisplayName("생성자에 입력된 값들로 회원 정보를 초기화 할 수 있다.")
    @Test
    void createWithAllArgsConstructorTest() {
        final User user = new User(ID, NAME, EMAIL, PASSWORD);

        assertThat(user).isNotNull();
        assertAll(() -> {
           assertThat(user.getId()).isEqualTo(ID);
           assertThat(user.getName()).isEqualTo(NAME);
           assertThat(user.getEmail()).isEqualTo(EMAIL);
           assertThat(user.getPassword()).isEqualTo(PASSWORD);
        });
    }

    @DisplayName("update 메서드는")
    @Nested
    class Describe_update {

        @DisplayName("수정할 회원 정보가 주어지면")
        @Nested
        class Context_with_update_user_data {

            private User ORIGINAL_USER;
            private String UPDATE_PREFIX = "foo";
            private User USER_TO_UPDATE = User.of(UPDATE_PREFIX + NAME, UPDATE_PREFIX + EMAIL);

            @BeforeEach
            void setup() {
                ORIGINAL_USER = User.of(NAME, EMAIL);
            }

            @DisplayName("수정된 결과를 반환한다.")
            @Test
            void it_returns_updated_user() {
                ORIGINAL_USER.update(USER_TO_UPDATE);

                assertThat(ORIGINAL_USER.getEmail()).isEqualTo(USER_TO_UPDATE.getEmail());
            }
        }
    }

    @DisplayName("authenticate 메서드는")
    @Nested
    class Describe_authenticate {

        @DisplayName("올바른 비밀번호가 주어지면")
        @Nested
        class Context_with_correct_password {

            private User user = User.of(NAME, EMAIL);

            @BeforeEach
            void setup() {
                user.changePassword(PASSWORD, passwordEncoder);
            }

            @DisplayName("true를 반환한다.")
            @Test
            void will_return_true() {
                assertThat(user.authenticate(PASSWORD, passwordEncoder)).isTrue();
            }
        }

        @DisplayName("올바르지 않은 비밀번호가 주어지면")
        @Nested
        class Context_with_incorrect_password {

            private User user = User.of(NAME, EMAIL);
            private final String INCORRECT_PASSWORD = PASSWORD + "fail";

            @BeforeEach
            void setup() {
                user.changePassword(PASSWORD, passwordEncoder);
            }

            @DisplayName("false를 반환한다.")
            @Test
            void will_return_false() {
                assertThat(user.authenticate(INCORRECT_PASSWORD, passwordEncoder)).isFalse();
            }
        }
    }

    @DisplayName("changePassword 메서드는")
    @Nested
    class Describe_change_password {

        @DisplayName("암호화된 비밀번호를 초기화한다.")
        @Test
        void it_init_password_field() {
            User user = User.of("홍길동", "test@codesooem.com");
            user.changePassword(PASSWORD, passwordEncoder);

            assertThat(user.getPassword()).isNotEqualTo(PASSWORD);
            assertThat(passwordEncoder.matches(PASSWORD, user.getPassword())).isTrue();
        }
    }

    @DisplayName("isDeleted 메서드는")
    @Nested
    class Describe_is_deleted {

        @DisplayName("삭제 상태가 아니라면")
        @Nested
        class Context_when_not_invoked_destroy {

            final User user = User.of("홍길동", "test@codesooem.com");

            @DisplayName("false를 반환한다.")
            @Test
            void it_returns_false() {
                assertThat(user.isDeleted()).isFalse();
            }
        }

        @DisplayName("삭제 상태라면")
        @Nested
        class Context_when_invoked_destroy {

            final User user = User.of("홍길동", "test@codesooem.com");

            @BeforeEach
            void setup() {
                user.destroy();
            }

            @DisplayName("true를 반환한다.")
            @Test
            void it_returns_false() {
                assertThat(user.isDeleted()).isTrue();
            }
        }

    }

}
