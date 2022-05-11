package com.codesoom.assignment.controller;

import com.codesoom.assignment.AutoConfigureUtf8MockMvc;
import com.codesoom.assignment.domain.TestUserRepositoryDouble;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.session.SessionRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureUtf8MockMvc
@DisplayName("SessionController MockMVC 에서")
class SessionControllerWebTest {
    private static final String USERNAME = "username";
    private static final String EMAIL = "session@example.com";
    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUserRepositoryDouble userRepository;

    /**
     * 하나의 User를 생성해 등록
     * @return 생성한 User
     */
    private User createUser() {
        User user = User.of(
                USERNAME,
                EMAIL,
                PASSWORD
        );
        return userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST - /session/login")
    class Describe_of_login {

        @Nested
        @DisplayName("로그인 할 수 있는 정보가 주어지면")
        class Context_with_valid_data {
            private String email;
            private String password;

            @BeforeEach
            void setUp() {
                User user = createUser();
                email = user.getEmail();
                password = user.getPassword();
            }

            @Test
            @DisplayName("accessToken을 반환한다")
            void it_return_accessToken() throws Exception {
                SessionRequestDto sessionRequestDto = SessionRequestDto
                        .builder()
                        .email(email)
                        .password(password)
                        .build();
                mockMvc.perform(post("/session/login")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionRequestDto)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").exists());
            }
        }
    }

}