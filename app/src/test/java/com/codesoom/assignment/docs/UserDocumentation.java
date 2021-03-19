package com.codesoom.assignment.docs;

import com.codesoom.assignment.docs.common.Item;
import com.codesoom.assignment.docs.common.ItemProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class UserDocumentation {

    private static final Item AUTHORIZATION = Item.of("Authorization", "사용자 인증 수단, 액세스 토큰 값");

    private static final Item ID = Item.of("id", NUMBER, "회원 식별자");
    private static final Item EMAIL = Item.of("email", STRING, "회원 이메일");
    private static final Item NAME = Item.of("name", STRING, "회원 이름");
    private static final Item PASSWORD = Item.of("password", STRING, "회원 비밀번호");

    public static RestDocumentationResultHandler createUser() {
        return document("create-user",
                requestFields(
                        new ItemProvider(EMAIL, NAME, PASSWORD).toFields()
                ),
                responseFields(
                        new ItemProvider(ID, EMAIL, NAME).toFields()
                ));
    }

    public static RestDocumentationResultHandler updateUser() {
        return document("update-user",
                requestHeaders(
                        new ItemProvider(AUTHORIZATION).toHeaders()
                ),
                pathParameters(
                        new ItemProvider(ID).toParameters()
                ),
                requestFields(
                        new ItemProvider(NAME, PASSWORD).toFields()
                ),
                responseFields(
                        new ItemProvider(ID, EMAIL, NAME).toFields()
                ));
    }

    public static RestDocumentationResultHandler deleteUser() {
        return document("delete-user",
                requestHeaders(
                        new ItemProvider(AUTHORIZATION).toHeaders()
                ),
                pathParameters(
                        new ItemProvider(ID).toParameters()
                )
        );
    }

}
