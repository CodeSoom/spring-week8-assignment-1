package com.codesoom.assignment.controller.users;

import com.codesoom.assignment.TokenGenerator;
import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.domain.users.Role;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.domain.users.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static com.codesoom.assignment.ConstantsForTest.TOKEN_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@DisplayName("UserDeleteController 클래스")
public class UserDeleteControllerMockMvcTest extends ControllerTest {

    private final String EMAIL = "kimcs@codesoom.com";
    private final String ADMIN_EMAIL = "admin@codesoom.com";
    private final String PASSWORD = "rlacjftn098";

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
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    User saveUser() {
        User user = User.of("김철수", EMAIL);
        user.changePassword(PASSWORD, passwordEncoder);

        return repository.save(user);
    }

    User saveAdminUser() {
        User user = User.of("관리자",
                ADMIN_EMAIL,
                Arrays.asList(new Role(UserRole.USER), new Role(UserRole.ADMIN)));
        user.changePassword(PASSWORD, passwordEncoder);

        return repository.save(user);
    }

    @DisplayName("[DELETE] /users/{id}")
    @Nested
    class Describe_delete_user {

        @DisplayName("ADMIN 권한을 가진 회원이고")
        @Nested
        class Context_with_admin_user {

            private String ADMIN_TOKEN;

            @BeforeEach
            void setup() throws Exception {
                saveAdminUser();
                this.ADMIN_TOKEN = TokenGenerator.generateToken(mockMvc, ADMIN_EMAIL, PASSWORD);
            }

            @DisplayName("찾을 수 있는 회원 id가 주어진다면")
            @Nested
            class Context_with_exist_user {

                private Long EXIST_USER_ID;

                @BeforeEach
                void setup() {
                    this.EXIST_USER_ID = saveUser().getId();
                }

                @DisplayName("성공적으로 회원 정보를 삭제한다.")
                @Test
                void it_will_delete_user() throws Exception {
                    mockMvc.perform(delete("/users/{id}", EXIST_USER_ID)
                                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + ADMIN_TOKEN))
                            .andExpect(status().isNoContent())
                            .andDo(document("delete-users"
                                    , pathParameters(
                                            parameterWithName("id").description("회원 식별자"))
                                    , requestHeaders(
                                            headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰"))));

                    assertThat(repository.findById(EXIST_USER_ID).get().isDeleted()).isTrue();
                }
            }

            @DisplayName("찾을 수 없는 회원 id가 주어진다면")
            @Nested
            class Context_with_not_exist_user {

                private final Long NOT_EXIST_USER_ID = 999L;

                @BeforeEach
                void setup() {
                    if (repository.existsById(NOT_EXIST_USER_ID)) {
                        repository.deleteById(NOT_EXIST_USER_ID);
                    }
                }

                @DisplayName("404 user not found를 응답한다.")
                @Test
                void it_response_404_user_not_found() throws Exception {
                    mockMvc.perform(delete("/users/" + NOT_EXIST_USER_ID)
                                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + ADMIN_TOKEN))
                            .andExpect(status().isNotFound());
                }
            }
        }

        @DisplayName("ADMIN 권한이 없는 회원이 요청하면")
        @Nested
        class Context_with_not_authorize {
            private Long USER_ID;
            private String TOKEN;

            @BeforeEach
            void setup() throws Exception {
                User user = saveUser();
                this.USER_ID = user.getId();
                this.TOKEN = TokenGenerator.generateToken(mockMvc, EMAIL, PASSWORD);
            }

            @DisplayName("403 forbidden을 응답한다.")
            @Test
            void it_response_403_forbidden() throws Exception {
                mockMvc.perform(delete("/users/" + USER_ID)
                                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN))
                        .andExpect(status().isForbidden());
            }
        }
    }

}
