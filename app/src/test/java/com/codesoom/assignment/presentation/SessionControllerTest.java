package com.codesoom.assignment.presentation;

import com.codesoom.assignment.MockMvcCharacterEncodingCustomizer;
import com.codesoom.assignment.adapter.in.web.session.SessionController;
import com.codesoom.assignment.common.utils.JsonUtil;
import com.codesoom.assignment.session.application.port.in.AuthenticationUseCase;
import com.codesoom.assignment.session.exception.LoginFailException;
import com.codesoom.assignment.support.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_정상_토큰;
import static com.codesoom.assignment.support.UserFixture.회원_1번;
import static com.codesoom.assignment.support.UserFixture.회원_1번_틀린_비밀번호;
import static com.codesoom.assignment.support.UserFixture.회원_비밀번호_비정상;
import static com.codesoom.assignment.support.UserFixture.회원_이메일_비정상;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@Import(MockMvcCharacterEncodingCustomizer.class)
@DisplayName("SessionController 웹 유닛 테스트")
class SessionControllerTest {
    private static final String REQUEST_SESSION_URL = "/session";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationUseCase authenticationUseCase;

    /**
     * Spring Boot 2.3 버전까지 mock이 각 테스트 종료 후 reset되지 않는 이슈 존재 <br>
     * 따라서 각 테스트 실행 전 mock의 invoke 횟수를 수동으로 초기화 시켜줍니다.
     * <br><br>
     * Ref: https://github.com/spring-projects/spring-boot/issues/12470
     */
    @BeforeEach
    void setUpClearMock() {
        Mockito.clearInvocations(authenticationUseCase);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 로그인_API는 {

        @Nested
        @DisplayName("찾을 수 없는 이메일이 주어지면")
        class Context_with_invalid_email {
            private final String 찾을_수_없는_이메일 = 회원_1번.이메일();

            @BeforeEach
            void setUp() {
                given(authenticationUseCase.login(eq(찾을_수_없는_이메일), any(String.class)))
                        .willThrow(new LoginFailException());
            }

            @Test
            @DisplayName("400 코드로 응답한다")
            void it_responses_400() throws Exception {
                ResultActions perform = 로그인_API_요청(회원_1번);

                perform.andExpect(status().isBadRequest());

                verify(authenticationUseCase).login(eq(찾을_수_없는_이메일), any(String.class));
            }
        }

        @Nested
        @DisplayName("틀린 비밀번호가 주어지면")
        class Context_with_invalid_password {
            private final String 틀린_비밀번호 = 회원_1번_틀린_비밀번호.비밀번호();

            @BeforeEach
            void setUp() {
                given(authenticationUseCase.login(any(String.class), eq(틀린_비밀번호)))
                        .willThrow(new LoginFailException());
            }

            @Test
            @DisplayName("400 코드로 응답한다")
            void it_responses_400() throws Exception {
                ResultActions perform = 로그인_API_요청(회원_1번_틀린_비밀번호);

                perform.andExpect(status().isBadRequest());

                verify(authenticationUseCase).login(any(String.class), eq(틀린_비밀번호));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 회원 정보가 주어진다면")
        class Context_with_invalid_user {

            @Nested
            @DisplayName("이메일이 공백일 경우")
            class Context_with_empty_email {

                @Test
                @DisplayName("400 코드로 응답한다")
                void it_responses_400() throws Exception {
                    ResultActions perform = 로그인_API_요청(회원_이메일_비정상);

                    perform.andExpect(status().isBadRequest());

                    verify(authenticationUseCase, never()).login(any(String.class), any(String.class));
                }
            }

            @Nested
            @DisplayName("비밀번호가 4글자 미만일 경우")
            class Context_with_invalid_password {

                @Test
                @DisplayName("400 코드로 응답한다")
                void it_responses_400() throws Exception {
                    ResultActions perform = 로그인_API_요청(회원_비밀번호_비정상);

                    perform.andExpect(status().isBadRequest());

                    verify(authenticationUseCase, never()).login(any(String.class), any(String.class));
                }
            }
        }

        @Nested
        @DisplayName("유효한 정보가 주어지면")
        class Context_with_valid_login {

            @BeforeEach
            void setUp() {
                given(authenticationUseCase.login(회원_1번.이메일(), 회원_1번.비밀번호()))
                        .willReturn(유저_1번_정상_토큰.토큰_값());
            }

            @Test
            @DisplayName("201 코드로 응답한다")
            void it_responses_201() throws Exception {
                ResultActions perform = 로그인_API_요청(회원_1번);

                perform.andExpect(status().isCreated());
                perform.andExpect(content().string(containsString(유저_1번_정상_토큰.토큰_값())));

                verify(authenticationUseCase).login(회원_1번.이메일(), 회원_1번.비밀번호());
            }
        }
    }


    private ResultActions 로그인_API_요청(UserFixture userFixture) throws Exception {
        return mockMvc.perform(
                post(REQUEST_SESSION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(userFixture.로그인_요청_데이터_생성()))
        );
    }
}
