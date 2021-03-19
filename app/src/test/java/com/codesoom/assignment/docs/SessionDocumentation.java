package com.codesoom.assignment.docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class SessionDocumentation {

    public static RestDocumentationResultHandler createSession() {
        return document("create-session",
                requestFields(
                        fieldWithPath("email").type(STRING).description("회원 이메일"),
                        fieldWithPath("password").type(STRING).description("회원 비밀번호")
                ),
                responseFields(
                        fieldWithPath("accessToken").type(STRING).description("인증 토큰")
                ));
    }

}
