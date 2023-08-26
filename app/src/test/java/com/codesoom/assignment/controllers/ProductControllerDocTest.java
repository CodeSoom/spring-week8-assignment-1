package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.utils.RestDocsSupporter;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.ResultActions;


import java.util.Arrays;

import static com.codesoom.assignment.utils.TestHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("ProductController 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class ProductControllerDocTest extends RestDocsSupporter {

    @Test
    @DisplayName("상품 전체 조회 테스트")
    void RESTDOC_상품_전체_조회() throws Exception {
        given(productService.getProducts())
                .willReturn(Arrays.asList(TEST_PRODUCT));

        ResultActions result = mockMvc.perform(get("/products")
                .accept(APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(
                        document("product-inquiry-all",
                                responseFields(
                                        fieldWithPath("[].id").description("상품 아이디"),
                                        fieldWithPath("[].name").description("상품명"),
                                        fieldWithPath("[].price").description("가격"),
                                        fieldWithPath("[].maker").description("제조사"),
                                        fieldWithPath("[].imageUrl").description("이미지 URL")
                                )
                        ));
    }

    @Test
    @DisplayName("상품 단건 조회 테스트")
    void RESTDOC_상품_단건_조회() throws Exception {
        given(productService.getProduct(anyLong()))
                .willReturn(TEST_PRODUCT);

        ResultActions result = mockMvc.perform(get("/products/{productId}", 1L)
                .accept(APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(document("product-inquiry", pathParameters(
                                parameterWithName("productId").description("조회할 상품 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("상품 아이디"),
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("가격"),
                                fieldWithPath("maker").description("제조사"),
                                fieldWithPath("imageUrl").description("이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("상품 생성 테스트")
    void RESTDOC_상품_생성() throws Exception {
        given(productService.createProduct(any(ProductData.class)))
                .willReturn(TEST_PRODUCT);

        ResultActions result = mockMvc.perform(post("/products")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_PRODUCT_DATA))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN));


        result.andExpect(status().isCreated())
                .andDo(document("product-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("회원 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("가격"),
                                fieldWithPath("maker").description("제조사"),
                                fieldWithPath("imageUrl").description("이미지 URL")
                        ),
                        responseFields(
                                fieldWithPath("id").description("상품 아이디"),
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("가격"),
                                fieldWithPath("maker").description("제조사"),
                                fieldWithPath("imageUrl").description("이미지 URL")
                        )
                ));
    }


    @Test
    @DisplayName("상품 수정 테스트")
    void RESTDOC_상품_수정() throws Exception {
        given(productService.updateProduct(anyLong(), any(ProductData.class)))
                .willReturn(UPDATED_PRODUCT);

        ResultActions result = mockMvc.perform(patch("/products/{productId}", 1L)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                .content(objectMapper.writeValueAsString(UPDATE_PRODUCT_DATA)));

        result.andExpect(status().isOk())
                .andDo(document("product-update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("회원 인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("productId").description("수정 할 상품 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("가격"),
                                fieldWithPath("maker").description("제조사"),
                                fieldWithPath("imageUrl").description("이미지 URL")
                        ),
                        responseFields(
                                fieldWithPath("id").description("상품 아이디"),
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("가격"),
                                fieldWithPath("maker").description("제조사"),
                                fieldWithPath("imageUrl").description("이미지 URL")
                        )
                ));
    }

    @Test
    void RESTDOC_상품_삭제() throws Exception {
        given(productService.deleteProduct(anyLong()))
                .willReturn(TEST_PRODUCT);

        ResultActions result = mockMvc.perform(delete("/products/{productId}", 1L)
                .accept(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN));

        result.andExpect(status().isOk())
                .andDo(document("product-delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("회원 인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("productId").description("삭제 할 상품 아이디")
                        )
                ));
    }
}
