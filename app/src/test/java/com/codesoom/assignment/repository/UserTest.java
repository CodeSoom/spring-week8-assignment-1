package com.codesoom.assignment.repository;

import com.codesoom.assignment.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.codesoom.assignment.support.UserFixture.회원_1번;
import static com.codesoom.assignment.support.UserFixture.회원_2번;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class update_메서드는 {

        @Test
        @DisplayName("비어있지 않은 값들을 수정한다")
        void it_update_user() {
            User user = 회원_1번.회원_엔티티_생성();

            User userUpdate = User.builder()
                    .name(회원_2번.이름())
                    .password(회원_2번.비밀번호())
                    .build();

            user.update(userUpdate, passwordEncoder);

            assertThat(user.getName()).isEqualTo(회원_2번.이름());
            assertThat(passwordEncoder.matches(회원_2번.비밀번호(), user.getPassword())).isTrue();
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class changePassword_메서드는 {

        @Test
        @DisplayName("비밀번호를 변경한다")
        void it_change_password() {
            User user = 회원_1번.회원_엔티티_생성();

            user.changePassword(회원_2번.비밀번호(), passwordEncoder);

            assertThat(passwordEncoder.matches(회원_2번.비밀번호(), user.getPassword())).isTrue();
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class destroy_메서드는 {

        @Test
        @DisplayName("deleted 값을 true로 변경한다.")
        void it_change_deleted_true() {
            User user = 회원_1번.회원_엔티티_생성();

            assertThat(user.isDeleted()).isFalse();

            user.destroy();

            assertThat(user.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class authenticate_메서드는 {

        @Nested
        @DisplayName("삭제된 계정이라면")
        class Context_with_deleted_user {
            private User 삭제된_계정;

            @BeforeEach
            void setUp() {
                삭제된_계정 = User.builder()
                        .id(회원_1번.아이디())
                        .name(회원_1번.이름())
                        .email(회원_1번.이메일())
                        .password(회원_1번.비밀번호())
                        .deleted(true)
                        .build();
            }

            @Test
            @DisplayName("비밀번호 인증 성공 여부와 관계없이 false를 반환한다")
            void it_returns_false() {
                assertThat(
                        삭제된_계정.authenticate(회원_1번.비밀번호(), passwordEncoder)
                ).isFalse();

                assertThat(
                        삭제된_계정.authenticate(회원_2번.비밀번호(), passwordEncoder)
                ).isFalse();
            }
        }

        @Nested
        @DisplayName("삭제된 계정이 아니라면")
        class Context_with_not_deleted_user {

            @Test
            @DisplayName("비밀번호가 맞는지 검증하여 true/false로 리턴한다")
            void it_validate_password() {
                User user = 회원_1번.회원_엔티티_생성();

                assertThat(
                        user.authenticate(회원_1번.비밀번호(), passwordEncoder)
                ).isTrue();

                assertThat(
                        user.authenticate(회원_2번.비밀번호(), passwordEncoder)
                ).isFalse();
            }
        }
    }
}
