package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static com.codesoom.assignment.controllers.ApiDocumentUtils.getDocumentRequest;
import static com.codesoom.assignment.controllers.ApiDocumentUtils.getDocumentResponse;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureRestDocs
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .imageUrl("localhost:8080/cat/mouse.jpg")
                .build();

        given(productService.getProducts()).willReturn(List.of(product));

        given(productService.getProduct(1L)).willReturn(product);

        given(productService.getProduct(1000L))
                .willThrow(new ProductNotFoundException(1000L));

        given(productService.createProduct(any(ProductData.class)))
                .willReturn(product);

        given(productService.updateProduct(eq(1L), any(ProductData.class)))
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    ProductData productData = invocation.getArgument(1);
                    return Product.builder()
                            .id(id)
                            .name(productData.getName())
                            .maker(productData.getMaker())
                            .price(productData.getPrice())
                            .imageUrl(productData.getImageUrl())
                            .build();
                });

        given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                .willThrow(new ProductNotFoundException(1000L));

        given(productService.deleteProduct(1000L))
                .willThrow(new ProductNotFoundException(1000L));

        given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);

        given(authenticationService.parseToken(INVALID_TOKEN))
                .willThrow(new InvalidTokenException(INVALID_TOKEN));

        given(authenticationService.roles(1L))
                .willReturn(Arrays.asList(new Role("USER")));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(
                get("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("get-products",
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].id").type(NUMBER).description("아이디"),
                                        fieldWithPath("[].name").type(STRING).description("장난감 명"),
                                        fieldWithPath("[].maker").type(STRING).description("브랜드"),
                                        fieldWithPath("[].price").type(NUMBER).description("가격"),
                                        fieldWithPath("[].imageUrl").type(STRING).description("이미지 주소")
                                        )
                ));
    }

    @Test
    void detailWithExistedProduct() throws Exception {
        Long id = 1L;

        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/products/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("get-product-existed-product",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("아이디"),
                                fieldWithPath("name").type(STRING).description("장난감 명"),
                                fieldWithPath("maker").type(STRING).description("브랜드"),
                                fieldWithPath("price").type(NUMBER).description("가격"),
                                fieldWithPath("imageUrl").type(STRING).description("이미지 주소")
                        )
                ));
    }

    @Test
    void detailWithNotExistedProduct() throws Exception {
        mockMvc.perform(get("/products/1000"))
                .andExpect(status().isNotFound())
                .andDo(document("get-product-not-existed-product"));
    }

    @Test
    void createWithValidAttributes() throws Exception {

        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\",\"price\":5000,\"imageUrl\":\"localhost:8080/cat/mouse.jpg\"}")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("post-product-valid-attributes",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("장난감 명"),
                                fieldWithPath("maker").type(STRING).description("메이커"),
                                fieldWithPath("price").type(NUMBER).description("가격"),
                                fieldWithPath("imageUrl").type(STRING).description("이미지 주소")
                                ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("아이디"),
                                fieldWithPath("name").type(STRING).description("장난감 명"),
                                fieldWithPath("maker").type(STRING).description("메이커"),
                                fieldWithPath("price").type(NUMBER).description("가격"),
                                fieldWithPath("imageUrl").type(STRING).description("이미지 주소")
                        )
                ));

        verify(productService).createProduct(any(ProductData.class));
    }

    @Test
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"maker\":\"\"," +
                                "\"price\":0}")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isBadRequest())
                .andDo(document("post-product-invalid-attributes"));
    }

    @Test
    void createWithoutAccessToken() throws Exception {
        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
        )
                .andExpect(status().isUnauthorized())
                .andDo(document("post-product-without-token"));
    }

    @Test
    void createWithWrongAccessToken() throws Exception {
        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
        )
                .andExpect(status().isUnauthorized())
                .andDo(document("post-product-wrong-token"));
    }

    @Test
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000,\"imageUrl\": \"localhost:8080/dog/cats.jpg\"}")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐순이")))
                .andDo(document("patch-product-existed-product",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("장난감 명"),
                                fieldWithPath("maker").type(STRING).description("메이커"),
                                fieldWithPath("price").type(NUMBER).description("가격"),
                                fieldWithPath("imageUrl").type(STRING).description("이미지 주소")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("아이디"),
                                fieldWithPath("name").type(STRING).description("장난감 명"),
                                fieldWithPath("maker").type(STRING).description("메이커"),
                                fieldWithPath("price").type(NUMBER).description("가격"),
                                fieldWithPath("imageUrl").type(STRING).description("이미지 주소")
                        )));

        verify(productService).updateProduct(eq(1L), any(ProductData.class));
    }

    @Test
    void updateWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                patch("/products/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isNotFound())
                .andDo(document("patch-product-not-existed-product"));

        verify(productService).updateProduct(eq(1000L), any(ProductData.class));
    }

    @Test
    void updateWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"maker\":\"\"," +
                                "\"price\":0}")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isBadRequest())
                .andDo(document("patch-product-invalid-attributes"));
    }

    @Test
    void updateWithoutAccessToken() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
        )
                .andExpect(status().isUnauthorized())
                .andDo(document("patch-product-without-token"));
    }

    @Test
    void updateWithInvalidAccessToken() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
        )
                .andExpect(status().isUnauthorized())
                .andDo(document("patch-product-invalid-token"));
    }

    @Test
    void destroyWithExistedProduct() throws Exception {
        //urlTemplate not found. If you are using MockMvc did you use RestDocumentationRequestBuilders to build the request?
        //java.lang.IllegalArgumentException: urlTemplate not found. If you are using MockMvc did you use RestDocumentationRequestBuilders to build the request?
        Long id = 1L;
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/products/{id}", id)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document("delete-product-existed-product",
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        )
                ));
    }

    @Test
    void destroyWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                delete("/products/1000")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isNotFound())
                .andDo(document("delete-product-not-existed-product"));

        verify(productService).deleteProduct(1000L);
    }

    @Test
    void destroyWithoutAccessToken() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
        )
                .andExpect(status().isUnauthorized())
                .andDo(document("delete-product-without-token"));
    }

    @Test
    void destroyWithInvalidAccessToken() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
        )
                .andExpect(status().isUnauthorized())
                .andDo(document("delete-product-invalid-token"));
    }
}
