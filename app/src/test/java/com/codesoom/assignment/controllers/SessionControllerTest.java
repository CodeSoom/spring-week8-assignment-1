package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.errors.LoginFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@AutoConfigureRestDocs
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        given(authenticationService.login("tester@example.com", "test"))
                .willReturn("a.b.c");

        given(authenticationService.login("badguy@example.com", "test"))
                .willThrow(new LoginFailException("badguy@example.com"));

        given(authenticationService.login("tester@example.com", "xxx"))
                .willThrow(new LoginFailException("tester@example.com"));
    }

    @Test
    @DisplayName("login 메서드는 저장된 유저의 email과 password로 요청한 경우 유효한 accessToken을 리턴한다")
    void loginWithRightEmailAndPassword() throws Exception {
        mockMvc.perform(
                        post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"tester@example.com\"," +
                                        "\"password\":\"test\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")))
                .andDo(
                        document("post-session",
                                requestFields(
                                        fieldWithPath("email").description("유저의 email"),
                                        fieldWithPath("password").description("유저의 password")
                                ),
                                responseFields(
                                        fieldWithPath("accessToken").description("로그인의 성공한 유저의 token")
                                )
                        )
                );
    }

    @Test
    @DisplayName("login 메서드는 저장되어있지 않는 email로 요청한 경우 400을 리턴한다")
    void loginWithWrongEmail() throws Exception {
        mockMvc.perform(
                        post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"badguy@example.com\"," +
                                        "\"password\":\"test\"}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("login 메서드는 틀린 password 로 요청한 경우 400을 리턴한다")
    void loginWithWrongPassword() throws Exception {
        mockMvc.perform(
                        post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"tester@example.com\"," +
                                        "\"password\":\"xxx\"}")
                )
                .andExpect(status().isBadRequest());
    }
}
