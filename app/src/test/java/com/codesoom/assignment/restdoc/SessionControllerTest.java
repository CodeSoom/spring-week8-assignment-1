package com.codesoom.assignment.restdoc;

import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.support.RestDocsControllerSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.codesoom.assignment.support.FieldsItem.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
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
                                of("email", STRING, "이메일").toField(),
                                of("password", STRING, "비밀번호").toField()
                        ),
                        responseFields(
                                of("accessToken", STRING, "회원 식별 토큰").toField()
                        )
                ));
    }

    private SessionRequestData getSessionRequest() {
        return new SessionRequestData("tester@example.com", "test12345");
    }
}
