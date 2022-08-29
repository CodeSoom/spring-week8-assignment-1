package com.codesoom.assignment.controllers;

import com.codesoom.assignment.Fixture;
import com.codesoom.assignment.domain.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SessionController 클래스의")
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private final Map<String, String> LOGIN_DATA = Map.of(
            "email", Fixture.EMAIL,
            "password", Fixture.PASSWORD
    );

    private Map<String, String> postRequest(String path, Map<String, String> data) throws Exception {
        return objectMapper.readValue(
                mockMvc.perform(post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), new TypeReference<Map<String, String>>() {
                }
        );
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("일치하는 로그인 정보가 주어지면")
        class Context_loginData {
            @BeforeEach
            void prepare() throws Exception {
                Map<String, String> createdUser = postRequest(Fixture.USER_PATH, LOGIN_DATA);
            }

            @Test
            @DisplayName("토큰을 생성하고 201과 함께 응답한다")
            void It_returns_token() throws Exception {
                mockMvc.perform(post(Fixture.SESSION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(LOGIN_DATA)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.accessToken").isString());
            }
        }
    }
}
