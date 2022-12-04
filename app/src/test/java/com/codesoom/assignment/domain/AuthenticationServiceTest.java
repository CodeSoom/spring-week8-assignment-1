package com.codesoom.assignment.domain;

import com.codesoom.assignment.common.utils.JwtUtil;
import com.codesoom.assignment.role.repository.Role;
import com.codesoom.assignment.role.repository.RoleRepository;
import com.codesoom.assignment.session.application.AuthenticationService;
import com.codesoom.assignment.session.application.port.in.AuthenticationUseCase;
import com.codesoom.assignment.session.exception.InvalidTokenException;
import com.codesoom.assignment.session.exception.LoginFailException;
import com.codesoom.assignment.user.repository.User;
import com.codesoom.assignment.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codesoom.assignment.support.AuthHeaderFixture.관리자_1004번_정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_값_비정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_정상_토큰;
import static com.codesoom.assignment.support.UserFixture.회원_1번;
import static com.codesoom.assignment.support.UserFixture.회원_1번_틀린_비밀번호;
import static com.codesoom.assignment.support.UserFixture.회원_2번;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("AuthenticationService 유닛 테스트")
class AuthenticationServiceTest {
    private static final String SECRET = 유저_1번_정상_토큰.시크릿_키();

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private AuthenticationUseCase authenticationUseCase;


    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationUseCase = new AuthenticationService(userRepository, roleRepository, jwtUtil, passwordEncoder);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class login_메서드는 {

        @Nested
        @DisplayName("유효하지 않은 회원 정보가 주어지면")
        class Context_with_invalid_user {

            @Nested
            @DisplayName("찾을 수 없는 계정일 경우")
            class Context_with_not_exist_email {
                private final String 찾을_수_없는_계정 = 회원_2번.이메일();

                @BeforeEach
                void setUp() {
                    given(userRepository.findByEmail(찾을_수_없는_계정))
                            .willReturn(Optional.empty());
                }

                @Test
                @DisplayName("LoginFailException 예외를 던진다")
                void it_returns_exception() {
                    assertThatThrownBy(() -> authenticationUseCase.login(찾을_수_없는_계정, 회원_2번.비밀번호()))
                            .isInstanceOf(LoginFailException.class);

                    verify(userRepository).findByEmail(회원_2번.이메일());
                }
            }

            @Nested
            @DisplayName("틀린 비밀번호일 경우")
            class Context_with_exist_email {
                private final String 틀린_비밀번호 = passwordEncoder.encode(회원_1번_틀린_비밀번호.비밀번호());

                @BeforeEach
                void setUp() {
                    given(userRepository.findByEmail(회원_1번.이메일()))
                            .willReturn(Optional.of(회원_1번.회원_엔티티_생성(회원_1번.아이디())));
                }

                @Test
                @DisplayName("LoginFailException 예외를 던진다")
                void it_returns_exception() {
                    assertThatThrownBy(() -> authenticationUseCase.login(회원_1번.이메일(), 틀린_비밀번호))
                            .isInstanceOf(LoginFailException.class);

                    verify(userRepository).findByEmail(회원_1번.이메일());
                }
            }
        }

        @Nested
        @DisplayName("유효한 회원 정보가 주어지면")
        class Context_with_valid_user {

            @BeforeEach
            void setUp() {
                User user = 회원_1번.회원_엔티티_생성(회원_1번.아이디());

                given(userRepository.findByEmail(회원_1번.이메일()))
                        .willReturn(Optional.of(user));
            }

            @Test
            @DisplayName("토큰을 리턴한다")
            void it_returns_() {
                String token = authenticationUseCase.login(회원_1번.이메일(), 회원_1번.비밀번호());

                assertThat(token).isEqualTo(유저_1번_정상_토큰.토큰_값());

                verify(userRepository).findByEmail(회원_1번.이메일());
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class parseToken_메서드는 {

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다")
            void it_returns_excpetion() {
                assertThatThrownBy(() -> authenticationUseCase.parseToken(유저_1번_값_비정상_토큰.토큰_값()))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {

            @Test
            @DisplayName("회원 고유 id를 리턴한다")
            void it_returns_user_id() {
                Long userId = authenticationUseCase.parseToken(유저_1번_정상_토큰.토큰_값());

                assertThat(userId).isEqualTo(유저_1번_정상_토큰.아이디());
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class roles_메서드는 {

        @Nested
        @DisplayName("관리자 토큰이 주어지면")
        class Context_with_admin_token {

            @BeforeEach
            void setUp() {
                given(roleRepository.findAllByUserId(eq(관리자_1004번_정상_토큰.아이디())))
                        .willReturn(List.of(new Role(관리자_1004번_정상_토큰.아이디(), "ADMIN")));
            }

            @Test
            @DisplayName("\"ADMIN\"를 반환한다")
            void it_returs_admin() {
                assertThat(
                        authenticationUseCase.roles(관리자_1004번_정상_토큰.아이디()).stream()
                                .map(Role::getName)
                                .collect(Collectors.toList())
                ).isEqualTo(List.of("ADMIN"));
            }
        }

        @Nested
        @DisplayName("유저 토큰이 주어지면")
        class Context_with_user_token {

            @BeforeEach
            void setUp() {
                given(roleRepository.findAllByUserId(eq(유저_1번_정상_토큰.아이디())))
                        .willReturn(List.of(new Role(유저_1번_정상_토큰.아이디(), "USER")));
            }

            @Test
            @DisplayName("\"USER\"를 반환한다")
            void it_returs_users() {
                assertThat(
                        authenticationUseCase.roles(유저_1번_정상_토큰.아이디()).stream()
                                .map(Role::getName)
                                .collect(Collectors.toList())
                ).isEqualTo(List.of("USER"));
            }
        }
    }
}
