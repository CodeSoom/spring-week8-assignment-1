package com.codesoom.assignment.restdoc;

import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.support.RestDocsControllerSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그인 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class SessionControllerTest extends RestDocsControllerSupport {

    @Test
    void 로그인() throws Exception {
        given(authenticationService.login(anyString(), anyString()))
                .willReturn("생성된 회원 식별 토큰");

        ResultActions result = mockMvc.perform(post("/session")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getSessionRequest())));

        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("회원 식별 토큰")
                        )
                ));
    }

    private SessionRequestData getSessionRequest() {
        return new SessionRequestData("tester@example.com", "test12345");
    }
}
