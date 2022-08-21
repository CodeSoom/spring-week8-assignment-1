package com.codesoom.assignment.application;

import com.codesoom.assignment.Fixture;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("AuthenticationService 클래스의")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    public static final User USER = User.builder()
            .id(1L)
            .email(Fixture.EMAIL)
            .name(Fixture.USER_NAME)
            .password(Fixture.PASSWORD)
            .build();

    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
        userRepository = mock(UserRepository.class);
        authenticationService = new AuthenticationService(userRepository, jwtUtil);
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("입력한 로그인 정보와 일치하는 유저가 있다면")
        class Context_with_validLoginData {
            private SessionRequestData requestData;

            @BeforeEach
            void prepare() {
                requestData = new SessionRequestData(Fixture.EMAIL, Fixture.PASSWORD);

                given(userRepository.findByEmail(Fixture.EMAIL))
                        .willReturn(Optional.of(USER));
            }

            @Test
            @DisplayName("토큰을 생성하고 리턴한다")
            void It_returns_createdToken() {
                SessionResponseData token = authenticationService.login(requestData);

                Claims decode = jwtUtil.decode(token.getAccessToken());

                assertThat(decode)
                        .containsEntry("iss", "BJP")
                        .containsEntry("userId", 1)
                        .containsEntry("role", "USER");

                verify(userRepository).findByEmail(Fixture.EMAIL);
            }
        }
    }
}
