package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.errors.LoginFailException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private SessionRequestData validLoginData;
    private SessionRequestData invalidLoginData;
    private SessionResponseData token;

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    @BeforeEach
    void setUp() {
        token = SessionResponseData.builder()
                .accessToken(VALID_TOKEN)
                .build();

        given(authenticationService.login(any(SessionRequestData.class)))
                .willReturn(eq(VALID_TOKEN));
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("유효한 로그인 정보가 주어진다면")
        class Context_with_right_email_and_password {

            @BeforeEach
            void setUp() {
                validLoginData = SessionRequestData.builder()
                        .email("yhyojoo@codesoom.com")
                        .password("112233!!")
                        .build();
            }

            @Test
            @DisplayName("토큰을 발급한다")
            void loginWithRightEmailAndPassword() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginData))
                )
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(".")));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 로그인 정보가 주어진다면")
        class Context_with_wrong_email_and_password {

            @BeforeEach
            void setUp() {
                invalidLoginData = SessionRequestData.builder()
                        .email("hyo@codesoom.com")
                        .password("112233!!")
                        .build();

                given(authenticationService.login(any(SessionRequestData.class)))
                        .willThrow((new LoginFailException("hyo@codesoom.com"
                        )));
            }

            @Test
            @DisplayName("예외를 던진다")
            void loginWithWrongEmailAndPassword() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoginData))
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
