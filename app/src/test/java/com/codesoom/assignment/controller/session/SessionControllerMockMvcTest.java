package com.codesoom.assignment.controller.session;

import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.dto.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@DisplayName("SessionController 클래스")
public class SessionControllerMockMvcTest extends ControllerTest {

    private final String EMAIL = "test@codesoom.com";
    private final String PASSWORD = "password";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
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

    @DisplayName("[POST] /session")
    @Nested
    class Describe_post_login {

        @DisplayName("찾을 수 있는 이메일과")
        @Nested
        class Context_with_exist_user {

            @DisplayName("정확한 비밀번호가 주어지면")
            @Nested
            class Context_with_correct_password {

                private final SessionController.LoginRequestDto CORRECT_LOGIN_REQUEST_DTO
                        = new SessionController.LoginRequestDto(EMAIL, PASSWORD);

                @BeforeEach
                void setup() {
                    saveUser();
                }

                @AfterEach
                void cleanup() {
                    repository.deleteAll();
                }

                @DisplayName("토큰을 발급한다.")
                @Test
                void it_return_token () throws Exception {
                    final MvcResult result = mockMvc.perform(post("/session")
                            .content(objectMapper.writeValueAsString(CORRECT_LOGIN_REQUEST_DTO))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated())
                            .andDo(document("post-session"
                                    , requestFields(
                                            fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                            fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                    ), responseFields(
                                            fieldWithPath("accessToken").type(JsonFieldType.STRING).description("인증 토큰")
                                    )))
                            .andReturn();
                    final TokenResponse tokenResponse
                            = objectMapper.readValue(result.getResponse().getContentAsByteArray()
                            , TokenResponse.class);

                    assertThat(tokenResponse.getAccessToken()).isNotEmpty();
                }
            }

            @DisplayName("틀린 비밀번호가 주어지면")
            @Nested
            class Context_with_incorrect_password {
                private final String INCORRECT_PASSWORD = "fail" + PASSWORD;

                private final SessionController.LoginRequestDto INCORRECT_LOGIN_REQUEST_DTO
                        = new SessionController.LoginRequestDto(EMAIL, INCORRECT_PASSWORD);

                @BeforeEach
                void setup() {
                    saveUser();
                }

                @AfterEach
                void cleanup() {
                    repository.deleteAll();
                }

                @DisplayName("400 bad request를 응답한다.")
                @Test
                void it_throws_exception () throws Exception {
                    mockMvc.perform(post("/session")
                            .content(objectMapper.writeValueAsString(INCORRECT_LOGIN_REQUEST_DTO))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest());
                }
            }
        }

        @DisplayName("찾을 수 없는 회원 로그인 정보가 주어지면")
        @Nested
        class Context_with_not_exist_user {
            private final String NOT_EXIST_USER_EMAIL = "fail" + EMAIL;
            private final SessionController.LoginRequestDto LOGIN_REQUEST_DTO
                    = new SessionController.LoginRequestDto(NOT_EXIST_USER_EMAIL, PASSWORD);

            @BeforeEach
            void setup() {
                User user = repository.findByEmailAndDeletedIsFalse(NOT_EXIST_USER_EMAIL).orElse(null);
                if(user != null) {
                    repository.delete(user);
                }
            }

            @DisplayName("404 not found를 응답한다.")
            @Test
            void it_throws_user_not_found_exception() throws Exception {
                mockMvc.perform(post("/session")
                        .content(objectMapper.writeValueAsString(LOGIN_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }

}
