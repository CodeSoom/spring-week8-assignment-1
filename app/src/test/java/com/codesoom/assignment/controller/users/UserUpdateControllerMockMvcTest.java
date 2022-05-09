package com.codesoom.assignment.controller.users;

import com.codesoom.assignment.TokenGenerator;
import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.domain.users.UserResponseDto;
import com.codesoom.assignment.domain.users.UserSaveDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.codesoom.assignment.ConstantsForTest.TOKEN_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("UserUpdateController 클래스")
public class UserUpdateControllerMockMvcTest extends ControllerTest {

    private static final String EMAIL = "hgd@gmail.com";
    private static final String PASSWORD = "hgdZzang123";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
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

    @DisplayName("[PATCH] /users/{id}")
    @Nested
    class Describe_patch_users {

        @DisplayName("본인의 id와")
        @Nested
        class Context_with_authorized_user {

            private Long EXIST_ID;
            private String TOKEN;

            @BeforeEach
            void setup() throws Exception {
                this.EXIST_ID = saveUser().getId();
                this.TOKEN = TokenGenerator.generateToken(mockMvc, EMAIL, PASSWORD);
            }

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("유효한 수정 정보가 주어지면")
            @Nested
            class Context_with_valid_data {

                private final UserSaveDto UPDATE_TO_USER
                        = new UserSaveDto("심청이", "hyonyeoChung@gmail.com", "tlacjd123");

                @DisplayName("회원 정보를 수정한 뒤, 수정 결과를 응답한다.")
                @Test
                void it_returns_updated_user() throws Exception {
                    final MvcResult result = mockMvc.perform(patch("/users/" + EXIST_ID)
                                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN)
                            .accept(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(UPDATE_TO_USER))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn();

                    final UserResponseDto user
                            = objectMapper.readValue(result.getResponse().getContentAsByteArray(), UserResponseDto.class);

                    assertThat(user.getId()).isEqualTo(EXIST_ID);
                    assertThat(user.getName()).isEqualTo(UPDATE_TO_USER.getName());
                    assertThat(user.getEmail()).isEqualTo(UPDATE_TO_USER.getEmail());
                    assertThat(passwordEncoder.matches("tlacjd123",user.getPassword())).isTrue();
                }
            }

            @DisplayName("유효하지 않은 이름이 포함된 데이터가 주어지면")
            @Nested
            class Context_with_invalid_name {
                private final UserSaveDto EMPTY_NAME
                        = new UserSaveDto("", "hkd@codesoom.com", "password");
                private final UserSaveDto BLANK_NAME
                        = new UserSaveDto(" ", "hkd@codesoom.com", "password");
                private final UserSaveDto NULL_NAME
                        = new UserSaveDto(null, "hkd@codesoom.com", "password");
                private final List<UserSaveDto> INVALID_NAMES = List.of(EMPTY_NAME, BLANK_NAME, NULL_NAME);

                @DisplayName("400 bad request를 응답한다.")
                @Test
                void it_response_bad_request() throws Exception {
                    for (UserSaveDto invalidNameUserDto : INVALID_NAMES) {
                        mockMvc.perform(patch("/users/" + EXIST_ID)
                                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(invalidNameUserDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
                    }
                }
            }

            @DisplayName("유효하지 않은 이메일이 포함된 데이터가 주어지면")
            @Nested
            class Context_with_invalid_email {

                private final UserSaveDto EMPTY_EMAIL
                        = new UserSaveDto("홍길동", "", "password");
                private final UserSaveDto BLANK_EMAIL
                        = new UserSaveDto("홍길동",  " ", "password");
                private final UserSaveDto NULL_EMAIL
                        = new UserSaveDto("홍길동", null, "password");
                private final UserSaveDto INVALID_EMAIL
                        = new UserSaveDto("홍길동", "itsNotEmailFormat@", "password");
                private final List<UserSaveDto> INVALID_EMAILS
                        = List.of(EMPTY_EMAIL, BLANK_EMAIL, NULL_EMAIL, INVALID_EMAIL);

                @DisplayName("400 bad request를 응답한다.")
                @Test
                void it_response_bad_request() throws Exception {
                    for (UserSaveDto invalidEmailUserDto : INVALID_EMAILS) {
                        mockMvc.perform(patch("/users/" + EXIST_ID)
                                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(invalidEmailUserDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
                    }
                }
            }

            @DisplayName("유효하지 않은 비밀번호가 포함된 데이터가 주어지면")
            @Nested
            class Context_with_invalid_password {
                private final UserSaveDto EMPTY_PASSWORD
                        = new UserSaveDto("홍길동", "hkd@codesoom.com", "");
                private final UserSaveDto BLANK_PASSWORD
                        = new UserSaveDto("홍길동", "hkd@codesoom.com", " ");
                private final UserSaveDto NULL_PASSWORD
                        = new UserSaveDto("홍길동", "hkd@codesoom.com", null);
                private final UserSaveDto TOO_SHORT_PASSWORD
                        = new UserSaveDto("홍길동", "hkd@codesoom.com", "1234");
                private final UserSaveDto TOO_LONG_PASSWORD
                        = new UserSaveDto("홍길동", "hkd@codesoom.com"
                        , "qwertyuiopasdfghjklzxcvbnm123456789");
                private final List<UserSaveDto> INVALID_PASSWORDS
                        = List.of(EMPTY_PASSWORD, BLANK_PASSWORD, NULL_PASSWORD
                        ,TOO_SHORT_PASSWORD, TOO_LONG_PASSWORD);

                @DisplayName("400 bad request를 응답한다.")
                @Test
                void it_response_bad_request() throws Exception {
                    for (UserSaveDto invalidPasswordUserDto : INVALID_PASSWORDS) {
                        mockMvc.perform(patch("/users/" + EXIST_ID)
                                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(invalidPasswordUserDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
                    }
                }
            }
        }

        @DisplayName("본인이 아닌 id가 주어지면")
        @Nested
        class Context_with_not_authorize {

            private Long NOT_USER_ID;
            private String TOKEN;
            private final UserSaveDto UPDATE_TO_USER
                    = new UserSaveDto("심청이", "hyonyeoChung@gmail.com", "tlacjd123");

            @BeforeEach
            void setup() throws Exception {
                final UserSaveDto userSaveDto
                        = new UserSaveDto("홍길동", "hgd@gmail.com", "hgdZzang123");
                this.NOT_USER_ID = saveUser().getId() + 100;
                this.TOKEN = TokenGenerator.generateToken(mockMvc, EMAIL, PASSWORD);
            }


            @DisplayName("403 forbidden을 응답한다.")
            @Test
            void it_response_403_forbidden() throws Exception {
                mockMvc.perform(patch("/users/" + NOT_USER_ID)
                                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(UPDATE_TO_USER))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isForbidden());
            }
        }
    }

}
