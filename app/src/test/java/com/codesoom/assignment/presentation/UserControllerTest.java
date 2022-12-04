package com.codesoom.assignment.presentation;

import com.codesoom.assignment.MockMvcCharacterEncodingCustomizer;
import com.codesoom.assignment.adapter.in.web.user.UserController;
import com.codesoom.assignment.adapter.in.web.user.dto.request.UserCreateRequestDto;
import com.codesoom.assignment.adapter.in.web.user.dto.request.UserUpdateRequestDto;
import com.codesoom.assignment.common.utils.JsonUtil;
import com.codesoom.assignment.support.AuthHeaderFixture;
import com.codesoom.assignment.support.UserFixture;
import com.codesoom.assignment.user.domain.port.in.UserUseCase;
import com.codesoom.assignment.user.exception.UserNotFoundException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.codesoom.assignment.support.AuthHeaderFixture.관리자_1004번_정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_값_비정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_2번_정상_토큰;
import static com.codesoom.assignment.support.UserFixture.회원_1번;
import static com.codesoom.assignment.support.UserFixture.회원_2번;
import static com.codesoom.assignment.support.UserFixture.회원_비밀번호_비정상;
import static com.codesoom.assignment.support.UserFixture.회원_이름_비정상;
import static com.codesoom.assignment.support.UserFixture.회원_이메일_비정상;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(MockMvcCharacterEncodingCustomizer.class)
@DisplayName("UserController 웹 유닛 테스트")
class UserControllerTest extends AuthenticationProvider {
    private static final String REQUEST_USER_URL = "/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserUseCase userUseCase;

    /**
     * Spring Boot 2.3 버전까지 mock이 각 테스트 종료 후 reset되지 않는 이슈 존재 <br>
     * 따라서 각 테스트 실행 전 mock의 invoke 횟수를 수동으로 초기화 시켜줍니다.
     * <br><br>
     * Ref: https://github.com/spring-projects/spring-boot/issues/12470
     */
    @BeforeEach
    void setUpClearMock() {
        Mockito.clearInvocations(userUseCase);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 회원_등록_API는 {

        @Nested
        @DisplayName("유효하지 않은 회원 정보가 주어지면")
        class Context_with_invalid_user {

            @Nested
            @DisplayName("이메일이 공백일 경우")
            class Context_with_empty_email {

                @Test
                @DisplayName("400 코드로 응답한다")
                void it_responses_400() throws Exception {
                    ResultActions perform = 회원_등록_API_요청(회원_이름_비정상);

                    perform.andExpect(status().isBadRequest());

                    verify(userUseCase, never()).createUser(any(UserCreateRequestDto.class));
                }
            }

            @Nested
            @DisplayName("이름이 공백일 경우")
            class Context_with_empty_name {

                @Test
                @DisplayName("400 코드로 응답한다")
                void it_responses_400() throws Exception {
                    ResultActions perform = 회원_등록_API_요청(회원_이메일_비정상);

                    perform.andExpect(status().isBadRequest());

                    verify(userUseCase, never()).createUser(any(UserCreateRequestDto.class));
                }
            }

            @Nested
            @DisplayName("비밀번호가 4글자 미만일 경우")
            class Context_with_invalid_password {

                @Test
                @DisplayName("400 코드로 응답한다")
                void it_responses_400() throws Exception {
                    ResultActions perform = 회원_등록_API_요청(회원_비밀번호_비정상);

                    perform.andExpect(status().isBadRequest());

                    verify(userUseCase, never()).createUser(any(UserCreateRequestDto.class));
                }
            }
        }

        @Nested
        @DisplayName("유효한 회원 정보가 주어지면")
        class Context_with_valid_user {

            @BeforeEach
            void setUp() {
                given(userUseCase.createUser(eq(회원_1번.등록_요청_데이터_생성())))
                        .willReturn(회원_1번.회원_엔티티_생성(회원_1번.아이디()));
            }

            @Test
            @DisplayName("201 코드로 응답한다")
            void it_responses_201() throws Exception {
                ResultActions perform = 회원_등록_API_요청(회원_1번);

                perform.andExpect(status().isCreated());

                verify(userUseCase).createUser(회원_1번.등록_요청_데이터_생성());
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 회원_수정_API는 {

        @Nested
        @DisplayName("인증 토큰이 없다면")
        class Context_with_token_not_exist {

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = mockMvc.perform(
                        patch(REQUEST_USER_URL + "/" + 회원_1번.아이디())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValueAsString(회원_1번.수정_요청_데이터_생성()))
                );

                perform.andExpect(status().isUnauthorized());

                verify(userUseCase, never())
                        .updateUser(any(Long.class), any(UserUpdateRequestDto.class), any(Long.class));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 인증 토큰이 주어지면")
        class Context_with_invalid_token_value {

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = 회원_수정_API_요청(
                        회원_1번.아이디(),
                        유저_1번_값_비정상_토큰,
                        회원_1번
                );

                perform.andExpect(status().isUnauthorized());

                verify(userUseCase, never())
                        .updateUser(any(Long.class), any(UserUpdateRequestDto.class), any(Long.class));
            }
        }

        @Nested
        @DisplayName("다른 사람의 인증 토큰이 주어지면")
        class Context_with_different_id_token_and_request {

            @BeforeEach
            void setUp() {
                given(userUseCase.updateUser(
                        eq(회원_1번.아이디()),
                        eq(회원_1번.수정_요청_데이터_생성()),
                        eq(유저_2번_정상_토큰.아이디())
                ))
                        .willThrow(new AccessDeniedException("Access denied"));
            }

            @Test
            @DisplayName("403 코드로 응답한다")
            void it_returns_403() throws Exception {
                ResultActions perform = 회원_수정_API_요청(
                        회원_1번.아이디(),
                        유저_2번_정상_토큰,
                        회원_1번
                );

                perform.andExpect(status().isForbidden());

                verify(userUseCase).updateUser(
                        eq(회원_1번.아이디()),
                        eq(회원_1번.수정_요청_데이터_생성()),
                        eq(유저_2번_정상_토큰.아이디())
                );
            }
        }

        @Nested
        @DisplayName("유효한 인증 토큰이 주어지고")
        class Context_with_valid_token_value {
            private final AuthHeaderFixture 유효한_인증_토큰 = 유저_1번_정상_토큰;

            @Nested
            @DisplayName("찾을 수 없는 id가 주어질 때")
            class Context_with_not_exist_id {

                @BeforeEach
                void setUp() {
                    given(userUseCase.updateUser(
                            eq(회원_1번.아이디()),
                            eq(회원_2번.수정_요청_데이터_생성()),
                            eq(유효한_인증_토큰.아이디())
                    ))
                            .willThrow(new UserNotFoundException());
                }

                @Test
                @DisplayName("404 코드로 응답한다")
                void it_responses_404() throws Exception {
                    ResultActions perform = 회원_수정_API_요청(
                            회원_1번.아이디(),
                            유효한_인증_토큰,
                            회원_2번
                    );

                    perform.andExpect(status().isNotFound());

                    verify(userUseCase).updateUser(
                            eq(회원_1번.아이디()),
                            eq(회원_2번.수정_요청_데이터_생성()),
                            eq(유효한_인증_토큰.아이디())
                    );
                }
            }

            @Nested
            @DisplayName("찾을 수 있는 id가 주어지고")
            class Context_with_exist_id {
                private final Long 찾을_수_있는_id = 회원_1번.아이디();

                @Nested
                @DisplayName("유효하지 않은 회원 정보가 주어진다면")
                class Context_with_invalid_user {

                    @Nested
                    @DisplayName("이름이 공백일 경우")
                    class Context_with_empty_name {

                        @Test
                        @DisplayName("400 코드로 응답한다")
                        void it_responses_400() throws Exception {
                            ResultActions perform = 회원_수정_API_요청(
                                    찾을_수_있는_id,
                                    유저_1번_정상_토큰,
                                    회원_이름_비정상
                            );

                            perform.andExpect(status().isBadRequest());

                            verify(userUseCase, never()).updateUser(
                                    any(Long.class),
                                    any(UserUpdateRequestDto.class),
                                    any(Long.class)
                            );
                        }
                    }

                    @Nested
                    @DisplayName("비밀번호가 4글자 미만일 경우")
                    class Context_with_invalid_password {

                        @Test
                        @DisplayName("400 코드로 응답한다")
                        void it_responses_400() throws Exception {
                            ResultActions perform = 회원_수정_API_요청(
                                    찾을_수_있는_id,
                                    유저_1번_정상_토큰,
                                    회원_비밀번호_비정상
                            );

                            perform.andExpect(status().isBadRequest());

                            verify(userUseCase, never()).updateUser(
                                    any(Long.class),
                                    any(UserUpdateRequestDto.class),
                                    any(Long.class)
                            );
                        }
                    }
                }

                @Nested
                @DisplayName("유효한 회원 정보가 주어진다면")
                class Context_with_valid_user {

                    @BeforeEach
                    void setUp() {
                        given(userUseCase.updateUser(
                                eq(찾을_수_있는_id),
                                eq(회원_1번.수정_요청_데이터_생성()),
                                eq(유저_1번_정상_토큰.아이디())
                        ))
                                .willReturn(회원_1번.회원_엔티티_생성(회원_1번.아이디()));
                    }

                    @Test
                    @DisplayName("200 코드로 응답한다")
                    void it_responses_200() throws Exception {
                        ResultActions perform = 회원_수정_API_요청(
                                찾을_수_있는_id,
                                유저_1번_정상_토큰,
                                회원_1번
                        );

                        perform.andExpect(status().isOk());

                        verify(userUseCase).updateUser(
                                eq(찾을_수_있는_id),
                                eq(회원_1번.수정_요청_데이터_생성()),
                                eq(유저_1번_정상_토큰.아이디())
                        );
                    }
                }
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 회원_삭제_API는 {

        @Nested
        @DisplayName("인증 토큰이 없다면")
        class Context_with_not_exist_token {

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = mockMvc.perform(
                        delete(REQUEST_USER_URL + "/" + 회원_1번.아이디())
                );

                perform.andExpect(status().isUnauthorized());

                verify(userUseCase, never()).deleteUser(회원_1번.아이디());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 인증 토큰이 주어지면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = 회원_삭제_API_요청(
                        회원_1번.아이디(),
                        유저_1번_값_비정상_토큰
                );

                perform.andExpect(status().isUnauthorized());

                verify(userUseCase, never()).deleteUser(any(Long.class));
            }
        }

        @Nested
        @DisplayName("관리자가 아닌 토큰이 주어지면")
        class Context_with_no_admin_token {

            @Test
            @DisplayName("403 코드로 응답한다")
            void it_responses_403() throws Exception {
                ResultActions perform = 회원_삭제_API_요청(
                        회원_1번.아이디(),
                        유저_1번_정상_토큰
                );

                perform.andExpect(status().isForbidden());

                verify(userUseCase, never()).deleteUser(any(Long.class));
            }
        }

        @Nested
        @DisplayName("관리자 토큰이 주어지고")
        class Context_with_admin_token {
            private final AuthHeaderFixture 관리자_토큰 = 관리자_1004번_정상_토큰;

            @Nested
            @DisplayName("찾을 수 없는 id가 주어지면")
            class Context_with_not_exist_id {
                private final Long 찾을_수_없는_id = 회원_2번.아이디();

                @BeforeEach
                void setUp() {
                    given(userUseCase.deleteUser(찾을_수_없는_id))
                            .willThrow(new UserNotFoundException());
                }

                @Test
                @DisplayName("404 코드로 응답한다")
                void it_responses_404() throws Exception {
                    ResultActions perform = 회원_삭제_API_요청(
                            찾을_수_없는_id,
                            관리자_토큰
                    );

                    perform.andExpect(status().isNotFound());

                    verify(userUseCase).deleteUser(찾을_수_없는_id);
                }
            }

            @Nested
            @DisplayName("찾을 수 있는 id가 주어지면")
            class Context_with_exist_id {
                private final Long 찾을_수_있는_id = 회원_1번.아이디();

                @Test
                @DisplayName("200 코드로 응답한다")
                void it_responses_200() throws Exception {
                    ResultActions perform = 회원_삭제_API_요청(
                            찾을_수_있는_id,
                            관리자_토큰
                    );

                    perform.andExpect(status().isOk());

                    verify(userUseCase).deleteUser(찾을_수_있는_id);
                }
            }
        }
    }


    private ResultActions 회원_등록_API_요청(final UserFixture userFixture) throws Exception {
        return mockMvc.perform(
                post(REQUEST_USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(userFixture.등록_요청_데이터_생성()))
        );
    }

    private ResultActions 회원_수정_API_요청(final Long userId,
                                       final AuthHeaderFixture authHeaderFixture,
                                       final UserFixture userFixture) throws Exception {
        return mockMvc.perform(
                patch(REQUEST_USER_URL + "/" + userId)
                        .header(HttpHeaders.AUTHORIZATION, authHeaderFixture.인증_헤더값())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(userFixture.수정_요청_데이터_생성()))
        );
    }

    private ResultActions 회원_삭제_API_요청(final Long userId,
                                       final AuthHeaderFixture authHeaderFixture) throws Exception {
        return mockMvc.perform(
                delete(REQUEST_USER_URL + "/" + userId)
                        .header(HttpHeaders.AUTHORIZATION, authHeaderFixture.인증_헤더값())
        );
    }
}
