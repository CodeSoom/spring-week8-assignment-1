package com.codesoom.assignment.controller;

import com.codesoom.assignment.AutoConfigureUtf8MockMvc;
import com.codesoom.assignment.application.user.UserCommandService;
import com.codesoom.assignment.domain.TestUserRepositoryDouble;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.user.UserModificationDto;
import com.codesoom.assignment.dto.user.UserRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureUtf8MockMvc
@DisplayName("UseRController MockMVC 에서")
class UserControllerWebTest {
    private static final String USERNAME = "사용자이름";
    private static final String EMAIL = "example@example.com";
    private static final String PASSWORD = "password";

    private static final String UPDATE_USERNAME = "변경된이름";
    private static final String UPDATE_PASSWORD = "password1234";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUserRepositoryDouble userRepository;

    @Autowired
    private UserCommandService userCommandService;

    /**
     * 하나의 User를 생성해 등록합니다.
     * @return 생성한 User
     */
    private User createUser() {
        UserRegistrationDto registrationDto = UserRegistrationDto
                .builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        return userCommandService.create(registrationDto);
    }

    @Nested
    @DisplayName("POST - /users")
    class Context_with_create_user {

        @Nested
        @DisplayName("생성에 필요한 데이터가 주어진다면")
        class Context_with_valid_body {
            private final UserRegistrationDto registrationDto = UserRegistrationDto
                    .builder()
                    .username(USERNAME)
                    .email(EMAIL)
                    .password(PASSWORD)
                    .build();

            @Test
            @DisplayName("사용자를 등록하고 등록한 사용자의 정보를 응답한다")
            void it_save_and_return_user() throws Exception {
                mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("username").exists())
                        .andExpect(jsonPath("email").exists())
                        .andExpect(jsonPath("password").doesNotExist());
            }
        }
    }

    @Nested
    @DisplayName("PATCH - /users/{id}")
    class Describe_of_patch {
        private User user;

        @BeforeEach
        void setUp() {
            user = createUser();
        }

        @Nested
        @DisplayName("업데이트할 수 있는 사용자의 id 와 업데이트할 정보가 주어지면")
        class Context_with_valid_id {
            private Long userId;
            private final UserModificationDto modificationDto = UserModificationDto
                    .builder()
                    .username(UPDATE_USERNAME)
                    .password(UPDATE_PASSWORD)
                    .build();

            @BeforeEach
            void setUp() {
                userId = user.getId();
            }

            @Test
            @DisplayName("사용자 정보를 수정하고 수정된 사용자 정보를 응답한다")
            void it_return_updated_user() throws Exception {
                mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificationDto)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("username").exists())
                        .andExpect(jsonPath("email").exists())
                        .andExpect(jsonPath("password").doesNotExist());
            }
        }

        @Nested
        @DisplayName("업데이트 할 수 없는 사용자의 id가 주어지면")
        class Context_with_invalid_id {
            private Long userId;
            private final UserModificationDto modificationDto = UserModificationDto
                    .builder()
                    .username(UPDATE_USERNAME)
                    .password(UPDATE_PASSWORD)
                    .build();

            @BeforeEach
            void setUp() {
                userId = user.getId();
                userRepository.deleteById(userId);
            }

            @Test
            @DisplayName("404 에러를 던진다")
            void it_throw_not_found() throws Exception {
                mockMvc.perform(patch("/users/{id}", userId)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(modificationDto)))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("DELETE - /users/{id}")
    class Describe_of_delete_user {
        private User user;

        @BeforeEach
        void setUp() {
            user = createUser();
        }

        @Nested
        @DisplayName("삭제할 수 있는 사용자의 id가 주어지면")
        class Context_with_valid_id {
            private Long userId;

            @BeforeEach
            void setUp() {
                userId = user.getId();
            }

            @Test
            @DisplayName("사용자를 삭제후 상태코드 204로 응답한다")
            void it_delete_user_return_no_content() throws Exception {
                mockMvc.perform(delete("/users/{id}", userId))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("삭제할 수 없는 사용자의 id가 주어지면")
        class Context_with_invalid_id {
            private Long userId;

            @BeforeEach
            void setUp() {
                userId = user.getId();
                userRepository.deleteById(userId);
            }

            @Test
            @DisplayName("상태코드 404로 응답한다")
            void it_throw_not_found() throws Exception {
                mockMvc.perform(delete("/users/{id}", userId))
                        .andExpect(status().isNotFound());
            }
        }
    }

}