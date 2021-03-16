package com.codesoom.assignment.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User 클래스")
class UserTest {
    private static final Long SETUP_ID = 1L;
    private static final String SETUP_NAME = "setupName";
    private static final String SETUP_EMAIL = "setupEmail";
    private static final String SETUP_PASSWORD = "setupPassword";
    private static final String UPDATE_NAME = "updateName";
    private static final String UPDATE_PASSWORD = "updatePassword";
    private static final String NOT_EXISTED_PASSWORD = "notExistedPassword";

    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(SETUP_PASSWORD);

        user = User.builder()
                .id(SETUP_ID)
                .name(SETUP_NAME)
                .email(SETUP_EMAIL)
                .password(encodedPassword)
                .build();
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Test
        @DisplayName("만약 사용자를 삭제한다면 삭제 여부가 true 로 바뀐다")
        void delete() {
            assertThat(user.isDeleted()).isFalse();

            user.delete();

            assertThat(user.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("updateName 메서드는")
    class Describe_updateName {
        @Nested
        @DisplayName("만약 수정 할 새로운 이름이 주어진다면")
        class Context_WithName {
            private final String givenName = UPDATE_NAME;

            @Test
            @DisplayName("주어진 이름으로 해당 사용자의 이름을 수정한다")
            void itUpdatesPassword() {
                assertThat(user.getName()).isEqualTo(SETUP_NAME);

                user.updateName(givenName);

                assertThat(user.getName()).isEqualTo(givenName);
            }
        }
    }

    @Nested
    @DisplayName("updatePassword 메서드는")
    class Describe_updatePassword {
        @Nested
        @DisplayName("만약 비밀번호가 주어진다면")
        class Context_WithPassword {
            private final String givenPassword = UPDATE_PASSWORD;
            private String encodedPassword;

            @BeforeEach
            void setUp() {
                encodedPassword = passwordEncoder.encode(SETUP_PASSWORD);
            }

            @Test
            @DisplayName("주어진 비밀번호로 해당 사용자의 비밀번호를 암호화한다")
            void itUpdatesPassword() {
                user.updatePassword(givenPassword, passwordEncoder);

                assertThat(user.getPassword()).isNotEmpty();
                assertThat(user.getPassword()).isNotEqualTo(encodedPassword);
            }
        }
    }

    @Nested
    @DisplayName("authenticate 메서드는")
    class Describe_authenticate {
        @Nested
        @DisplayName("만약 사용자가 삭제 되지 않았고 저장되어 있는 비밀번호가 주어진다면")
        class Context_UserNotDeletedAndWithExistedPassword {
            private final String givenExistedPassword = SETUP_PASSWORD;

            @Test
            @DisplayName("true 를 리턴한다")
            void itReturnsTrue() {
                assertThat(user.isDeleted()).isFalse();
                assertThat(user.authenticate(givenExistedPassword, passwordEncoder)).isTrue();
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 비밀번호가 주어진다면")
        class Context_WithNotExistedPassword {
            private final String givenNotExistedPassword = NOT_EXISTED_PASSWORD;

            @Test
            @DisplayName("false 를 리턴한다")
            void itReturnsTrue() {
                assertThat(user.isDeleted()).isFalse();
                assertThat(user.authenticate(givenNotExistedPassword, passwordEncoder)).isFalse();
            }
        }

        @Nested
        @DisplayName("만약 사용자가 삭제되었다면")
        class Context_UserDeleted {
            private User deletedUser;
            private String givenPassword = SETUP_PASSWORD;

            @BeforeEach
            void setUp() {
                String encodedPassword = passwordEncoder.encode(givenPassword);

                deletedUser = User.builder()
                        .id(SETUP_ID)
                        .name(SETUP_NAME)
                        .email(SETUP_EMAIL)
                        .password(encodedPassword)
                        .deleted(true)
                        .build();
            }

            @Test
            @DisplayName("false 를 리턴한다")
            void itReturnsFalse() {
                assertThat(deletedUser.isDeleted()).isTrue();
                assertThat(deletedUser.authenticate(givenPassword, passwordEncoder)).isFalse();
            }
        }
    }
}
