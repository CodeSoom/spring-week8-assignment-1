package com.codesoom.assignment.docs;

import com.codesoom.assignment.docs.common.Item;
import com.codesoom.assignment.docs.common.ItemProvider;
import com.codesoom.assignment.docs.common.State;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class ProductDocumentation {
    
    private static final Item AUTHORIZATION = Item.of("Authorization", "사용자 인증 수단, 액세스 토큰 값");

    private static final Item ID = Item.of("id", NUMBER, "상품 식별자");
    private static final Item NAME = Item.of("name", STRING, "상품 이름");
    private static final Item MAKER = Item.of("maker", STRING, "상품 제조사");
    private static final Item PRICE = Item.of("price", NUMBER, "상품 가격");
    private static final Item IMAGE_URL = Item.of("imageUrl", STRING, "상품 이미지", State.OPTIONAL);


    public static RestDocumentationResultHandler getProducts() {
        ItemProvider itemProvider = new ItemProvider(
                Item.of("[].id", NUMBER, "상품 식별자"),
                Item.of("[].name", STRING, "상품 이름"),
                Item.of("[].maker", STRING, "상품 제조사"),
                Item.of("[].price", NUMBER, "상품 가격"),
                Item.of("[].imageUrl", STRING, "상품 이미지", State.OPTIONAL)
        );

        return document("get-products",
                responseFields(itemProvider.toFields()));
    }

    public static RestDocumentationResultHandler getProduct() {
        return document("get-product",
                pathParameters(
                        new ItemProvider(ID).toParameters()
                ),
                responseFields(
                        new ItemProvider(ID, NAME, MAKER, PRICE, IMAGE_URL).toFields()
                ));
    }

    public static RestDocumentationResultHandler createProduct() {
        return document("create-product",
                requestHeaders(
                        new ItemProvider(AUTHORIZATION).toHeaders()
                ),
                requestFields(
                        new ItemProvider(NAME, MAKER, PRICE, IMAGE_URL).toFields()
                ),
                responseFields(
                        new ItemProvider(ID, NAME, MAKER, PRICE, IMAGE_URL).toFields()
                ));
    }

}
