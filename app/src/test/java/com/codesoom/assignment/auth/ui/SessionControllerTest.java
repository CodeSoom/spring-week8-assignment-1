package com.codesoom.assignment.auth.ui;

import com.codesoom.assignment.auth.application.AuthenticationService;
import com.codesoom.assignment.auth.application.LoginFailException;
import com.codesoom.assignment.common.RestDocConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@Import(RestDocConfiguration.class)
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
    void loginWithRightEmailAndPassword() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.
                        post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@example.com\"," +
                                "\"password\":\"test\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")))
                .andDo(print())
                .andDo(document("create-session",
                        requestFields(
                                fieldWithPath("email").type(STRING).description("이메일").optional()
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("password").type(STRING).description("비밀번호").optional()
                                        .attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(STRING).description("응답 토큰")
                        ))
                );
    }

    @Test
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
