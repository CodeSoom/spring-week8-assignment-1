package com.codesoom.assignment.controllers;

import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class UserTestFixture {
    public static PathParametersSnippet getUserPathParametersSnippet() {
        return pathParameters(
                parameterWithName("id").description("유저 아이디")
        );
    }

    public static RequestHeadersSnippet getUserRequestHeadersSnippet() {
        return requestHeaders(headerWithName("Authorization").description("인증 토큰"));
    }

    public static RequestFieldsSnippet getUserRegisterDataRequestFields() {
        return requestFields(
                fieldWithPath("email").type(STRING).description("유저 이메일"),
                fieldWithPath("name").type(STRING).description("유저 이름"),
                fieldWithPath("password").type(STRING).description("유저 비밀번호")
        );
    }

    public static RequestFieldsSnippet getUserModificationDataRequestFields() {
        return requestFields(
                fieldWithPath("name").type(STRING).description("유저 이름"),
                fieldWithPath("password").type(STRING).description("유저 비밀번호")
        );
    }

    public static ResponseFieldsSnippet getUserDataResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("id").type(NUMBER).description("유저 식별자"),
                fieldWithPath("email").type(STRING).description("유저 이메일"),
                fieldWithPath("name").type(STRING).description("유저 이름")
        );
    }
}
