package com.codesoom.assignment.session.controller;

import com.codesoom.assignment.session.exception.LoginFailException;
import com.codesoom.assignment.session.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjF9.mD0HZddWR7ufVRC_RyhCe_uUnB1ZF3XYM5kgfKFdEACQpLjIRoIozX4WqGYtSLqaSGGMhz2s1hovSn3QcG2_Og";

    @BeforeEach
    void setUp() {
        given(authenticationService.login("tester@example.com", "test"))
                .willReturn(TOKEN);

        given(authenticationService.login("badguy@example.com", "test"))
                .willThrow(new LoginFailException("badguy@example.com"));

        given(authenticationService.login("tester@example.com", "xxx"))
                .willThrow(new LoginFailException("tester@example.com"));
    }

    @Nested
    @DisplayName("login 메서드는")
    class testLogin {
        @Test
        @DisplayName("올바른 이메일과 비밀번호로 로그인하면 토큰을 응답한다.")
        void loginWithRightEmailAndPassword() throws Exception {
            mockMvc.perform(
                    post("/session")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\" : \"tester@example.com\", \"password\" : \"test\"}")
            )
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(TOKEN)));
        }

        @Test
        @DisplayName("잘못된 이메일로 로그인하면 토큰을 응답한다.")
        void loginWithWrongEmail() throws Exception {
            mockMvc.perform(
                    post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\" : \"badguy@example.com\", \"password\" : \"test\"}")
            )
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인하면 토큰을 응답한다.")
        void loginWithWrongPassword() throws Exception {
            mockMvc.perform(
                    post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\" : \"tester@example.com\", \"password\" : \"xxx\"}")
            )
                    .andExpect(status().isBadRequest());
        }
    }
}
