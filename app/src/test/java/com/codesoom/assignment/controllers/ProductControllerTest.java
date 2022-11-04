package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.application.RoleService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.security.UserAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @MockBean
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
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
                            .build();
                });

        given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                .willThrow(new ProductNotFoundException(1000L));

        given(productService.deleteProduct(1000L))
                .willThrow(new ProductNotFoundException(1000L));

        given(authenticationService.authenticate(VALID_TOKEN)).willReturn(
                new UserAuthentication(1L, Arrays.asList(new Role("USER"))));

        given(authenticationService.authenticate(INVALID_TOKEN))
                .willThrow(new InvalidTokenException(INVALID_TOKEN));
    }

    @Test
    @DisplayName("list 메서드는 product 의 리스트를 리턴한다")
    void list() throws Exception {
        mockMvc.perform(
                        get("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("get-products",
                        responseFields(
                                fieldWithPath("[].id").description("Product Id"),
                                fieldWithPath("[].name").description("Product 이름"),
                                fieldWithPath("[].maker").description("Product 제조사"),
                                fieldWithPath("[].price").description("Product 가격"),
                                fieldWithPath("[].imageUrl").description("Product 이미지 파일 주소")
                        )
                ));
    }

    @Test
    @DisplayName("detail 메서드는 요청한 product id 의 product 를 리턴한다")
    void deatilWithExsitedProduct() throws Exception {
        mockMvc.perform(
                        get("/products/{productId}", 1)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("get-product",
                                pathParameters(
                                        parameterWithName("productId").description("Product Id")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("Product Id"),
                                        fieldWithPath("name").description("Product 이름"),
                                        fieldWithPath("maker").description("Product 제조사"),
                                        fieldWithPath("price").description("Product 가격"),
                                        fieldWithPath("imageUrl").description("Product 이미지 파일 주소")
                                )
                        )
                );

    }

    @Test
    @DisplayName("detail 메서드는 요청한 product id 가 저장되어 있지 않는 경우 404 를 리턴한다")
    void deatilWithNotExsitedProduct() throws Exception {
        mockMvc.perform(get("/products/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("create 메서드는 새로운 product 를 생성하여 저장하고 리턴한다")
    void createWithValidAttributes() throws Exception {
        mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":\"5000\"}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("post-product",
                                requestHeaders(
                                        headerWithName("Authorization").description(
                                                "유효한 토큰")),
                                requestFields(
                                        fieldWithPath("name").description("Product 이름"),
                                        fieldWithPath("maker").description("Product 제조사"),
                                        fieldWithPath("price").description("Product 가격"),
                                        fieldWithPath("imageUrl").description("Product 이미지 파일 주소").optional().type(JsonFieldType.STRING)
                                ),
                                responseFields(
                                        fieldWithPath("id").description("Product Id"),
                                        fieldWithPath("name").description("Product 이름"),
                                        fieldWithPath("maker").description("Product 제조사"),
                                        fieldWithPath("price").description("Product 가격"),
                                        fieldWithPath("imageUrl").description("Product 이미지 파일 주소").optional().type(JsonFieldType.STRING)
                                )
                        )
                );

        verify(productService).createProduct(any(ProductData.class));
    }

    @Test
    @DisplayName("create 메서드는 유효하지 않은 attribute 로 요청한 경우 400을 리턴한다")
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"maker\":\"\"," +
                                        "\"price\":0}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("create 메서드는 Access Token 이 없이 요청한 경우 401을 리턴한다")
    void createWithoutAccessToken() throws Exception {
        mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("create 메서드는 유효하지 않은 Access Token 으로 요청한 경우 401을 리턴한다")
    void createWithWrongAccessToken() throws Exception {
        mockMvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("update 메서드는 요청한 product id 의 product 를 수정하고 리턴한다")
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(
                        patch("/products/{productId}", 1)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐순이")))
                .andDo(document("patch-product",
                                requestHeaders(
                                        headerWithName("Authorization").description("유효한 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("productId").description("Product Id")
                                ),
                                requestFields(
                                        fieldWithPath("name").description("Product 이름"),
                                        fieldWithPath("maker").description("Product 제조사"),
                                        fieldWithPath("price").description("Product 가격"),
                                        fieldWithPath("imageUrl").description("Product 이미지 파일 주소").optional().type(JsonFieldType.STRING)
                                ),
                                responseFields(
                                        fieldWithPath("id").description("Product Id"),
                                        fieldWithPath("name").description("Product 이름"),
                                        fieldWithPath("maker").description("Product 제조사"),
                                        fieldWithPath("price").description("Product 가격"),
                                        fieldWithPath("imageUrl").description("Product 이미지 파일 주소").optional().type(JsonFieldType.STRING)
                                )
                        )
                );

        verify(productService).updateProduct(eq(1L), any(ProductData.class));
    }

    @Test
    @DisplayName("update 메서드는 저장되지 않은 product Id로 요청한 경우 404를 리턴한다")
    void updateWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                        patch("/products/1000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isNotFound());

        verify(productService).updateProduct(eq(1000L), any(ProductData.class));
    }

    @Test
    @DisplayName("update 메서드는 유효하지 않은 attribute 로 요청한 경우 400을 리턴한다")
    void updateWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                        patch("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"maker\":\"\"," +
                                        "\"price\":0}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update 메서드는 Access Token 이 없이 요청한 경우 401을 리턴한다")
    void updateWithoutAccessToken() throws Exception {
        mockMvc.perform(
                        patch("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("update 메서드는 유효하지 않은 Access Token 으로 요청한 경우 401을 리턴한다")
    void updateWithInvalidAccessToken() throws Exception {
        mockMvc.perform(
                        patch("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("destroy 메서드는 요청한 product Id의 product 를 삭제한다")
    void destroyWithExistedProduct() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/products/{productId}", 1)
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(document("delete-product",
                        requestHeaders(
                                headerWithName("Authorization").description("유효한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("productId").description("Product Id")
                        ))
                );

        verify(productService).deleteProduct(1L);
    }

    @Test
    @DisplayName("destory 메서드는 요청한 product Id 의 product 가 저장되어 있지 않은 경우 404를 리턴한다")
    void destroyWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                        delete("/products/1000")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isNotFound());

        verify(productService).deleteProduct(1000L);
    }

    @Test
    @DisplayName("destroy 메서드는 Access Token 이 없이 요청한 경우 401을 리턴한다")
    void destroyWithoutAccessToken() throws Exception {
        mockMvc.perform(
                        delete("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("destroy 메서드는 유효하지 않은 Access Token 으로 요청한 경우 401을 리턴한다")
    void destroyWithInvalidAccessToken() throws Exception {
        mockMvc.perform(
                        delete("/products/1")
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized());
    }
}
