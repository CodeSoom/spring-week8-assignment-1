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

import static com.codesoom.assignment.helper.ApiDocumentUtils.getDocumentRequest;
import static com.codesoom.assignment.helper.ApiDocumentUtils.getDocumentResponse;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@AutoConfigureRestDocs
class SessionControllerTest {
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        given(authenticationService.login("tester@example.com", "test"))
                .willReturn(ACCESS_TOKEN);

        given(authenticationService.login("badguy@example.com", "test"))
                .willThrow(new LoginFailException("badguy@example.com"));

        given(authenticationService.login("tester@example.com", "xxx"))
                .willThrow(new LoginFailException("tester@example.com"));
    }

    @Test
    @DisplayName("이메일과 패스워드 정확할 경우 토큰을 반환한다")
    void returnsAccessTokenWhenRightEmailAndPassword() throws Exception {
        mockMvc.perform(
                post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@example.com\"," +
                                "\"password\":\"test\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")))
                .andDo(
                        document("login",
                                getDocumentRequest(),
                                getDocumentResponse())
                );
    }

    @Test
    @DisplayName("BAD REQUEST 응답코드를 반환한다")
    void returnsBadRequestWhenWrongEmail() throws Exception {
        mockMvc.perform(
                post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"badguy@example.com\"," +
                                "\"password\":\"test\"}")
        )
                .andExpect(status().isBadRequest())
                .andDo(
                        document("login-wrong-email",
                                getDocumentRequest(),
                                getDocumentResponse())
                );
    }

    @Test
    @DisplayName("BAD REQUEST 응답코드를 반환한다")
    void returnsBadRequestWhenWrongPassword() throws Exception {
        mockMvc.perform(
                post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@example.com\"," +
                                "\"password\":\"xxx\"}")
        )
                .andExpect(status().isBadRequest())
                .andDo(
                        document("login-wrong-password",
                                getDocumentRequest(),
                                getDocumentResponse())
                );
    }

//    @Nested
//    @DisplayName("login 메서드")
//    class DescribeLogin {
//        @Nested
//        @DisplayName("")
//        class Context {
//
//        }
//
//        @Nested
//        @DisplayName("이메일이 잘못된 경우")
//        class ContextWithWrongEmail {
//
//        }
//
//        @Nested
//        @DisplayName("패스워드가 잘못된 경우")
//        class ContextWithWrongPassword {
//
//        }
//    }
}
