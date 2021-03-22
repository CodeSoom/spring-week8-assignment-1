package com.codesoom.assignment.docs;

import com.codesoom.assignment.docs.common.Item;
import com.codesoom.assignment.docs.common.ItemProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class SessionDocumentation {

    private static final Item EMAIL = Item.of("email", STRING, "회원 이메일");
    private static final Item PASSWORD = Item.of("password", STRING, "회원 비밀번호");
    private static final Item ACCESS_TOKEN = Item.of("accessToken", STRING, "인증 토큰");

    public static RestDocumentationResultHandler createSession() {
        return document("create-session",
                requestFields(
                        new ItemProvider(EMAIL, PASSWORD).toFields()
                ),
                responseFields(
                        new ItemProvider(ACCESS_TOKEN).toFields()
                ));
    }

}
