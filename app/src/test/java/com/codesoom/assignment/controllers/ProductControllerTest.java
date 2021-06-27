package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class ProductControllerTest {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String PREFIX = "prefix";
    private static final String POSTFIX = "postfix";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    private static final Long EXISTED_PRODUCT_ID = 1L;
    private static final Long NOT_EXISTED_PRODUCT_ID = 1000L;
    private static final Long USER_ID = 1L;
    private static final String PRODUCT_NAME = "쥐돌이";
    private static final String PRODUCT_MAKER = "냥이월드";
    private static final String PRODUCT_IMAGE_URL = "www.abc.com";
    private static final String UPDATED_PRODUCT_NAME = "쥐순이";
    private static final int PRODUCT_PRICE = 5000;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    private Product product;

    private ProductData productData;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();

        product = Product.builder()
                .id(EXISTED_PRODUCT_ID)
                .name(PRODUCT_NAME)
                .maker(PRODUCT_MAKER)
                .price(PRODUCT_PRICE)
                .imageUrl(PRODUCT_IMAGE_URL)
                .build();

        productData = ProductData.builder()
                .name(PRODUCT_NAME)
                .maker(PRODUCT_MAKER)
                .price(PRODUCT_PRICE)
                .build();

        given(authenticationService.roles(USER_ID))
                .willReturn(Arrays.asList(new Role("USER")));
    }

    @Nested
    @DisplayName("GET /products 는")
    class Describe_list {

        @Nested
        @DisplayName("상품 목록 조회 요청이 들어오면")
        class Context_with_valid_request {

            @BeforeEach
            void prepareProductList() {
                given(productService.getProducts()).willReturn(Arrays.asList(product));
            }

            @Test
            @DisplayName("HttpStatus 200 OK를 응답한다")
            void it_returns_ok() throws Exception {
                mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(PRODUCT_NAME)))
                        .andDo(document("get-products"
//                                responseFields(
//                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("아이디"),
//                                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("이름"),
//                                        fieldWithPath("[].maker").type(JsonFieldType.STRING).description("제조회사"),
//                                        fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("가격"),
//                                        fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL"))
                        ));
            }
        }
    }

    @Nested
    @DisplayName("GET /products/{id} 는")
    class Describe_detail {

        @Nested
        @DisplayName("만약 존재하는 상품 id로 요청이 들어오면")
        class Context_with_existed_id {

            @BeforeEach
            void prepareDetail() {
                given(productService.getProduct(EXISTED_PRODUCT_ID)).willReturn(product);
            }

            @Test
            @DisplayName("HttpStatus 200 OK를 응답한다")
            void it_returns_ok() throws Exception {
                mockMvc.perform(get("/products/" + EXISTED_PRODUCT_ID)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(PRODUCT_NAME)))
                        .andDo(document("get-product"));
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 상품 id로 요청이 들어오면")
        class Context_with_not_existed_id {

            @BeforeEach
            void prepare() {
                given(productService.getProduct(NOT_EXISTED_PRODUCT_ID))
                        .willThrow(new ProductNotFoundException(NOT_EXISTED_PRODUCT_ID));
            }

            @Test
            @DisplayName("HttpStatus 404 Not Found를 응답한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(get("/products/" + NOT_EXISTED_PRODUCT_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("Jwt 토큰이 유효할때")
    class Context_with_jwt_token {

        @BeforeEach
        void prepare() {
            given(authenticationService.parseToken(VALID_TOKEN)).willReturn(USER_ID);
        }

        @Nested
        @DisplayName("POST /products 는")
        class Describe_create {

            @Nested
            @DisplayName("상품 생성 요청이 들어오면")
            class Context_with_valid_product_data {

                @BeforeEach
                void prepareCreate() {
                    given(productService.createProduct(any(ProductData.class)))
                            .willReturn(product);
                }

                @Test
                @WithMockUser(authorities = {"USER"})
                @DisplayName("HttpStatus 201 Created를 응답한다")
                void it_returns_created() throws Exception {
                    String content = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(post("/products")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                            .header(AUTHORIZATION, BEARER + VALID_TOKEN))
                            .andExpect(status().isCreated())
                            .andExpect(content().string(containsString(PRODUCT_NAME)));

                    verify(productService).createProduct(any(ProductData.class));
                }
            }

            @Nested
            @DisplayName("만약 빈 상품 생성 정보로 요청이 들어오면")
            class Context_with_invalid_product_data {

                @BeforeEach
                void prepareCreate() {
                    productData = ProductData.builder()
                            .build();
                }

                @Test
                @DisplayName("HttpStatus 400 Bad Request를 응답한다")
                void it_returns_bad_request() throws Exception {
                    String content = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(post("/products")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                            .header(AUTHORIZATION, BEARER + VALID_TOKEN))
                            .andExpect(status().isBadRequest());
                }
            }
        }

        @Nested
        @DisplayName("PATCH /products/{id} 는")
        class Describe_update {

            @Nested
            @DisplayName("만약 존재하는 상품 id로 요청이 들어오면")
            class Context_with_valid_product_data {

                @BeforeEach
                void prepareUpdate() {
                    productData = ProductData.builder()
                            .name(UPDATED_PRODUCT_NAME)
                            .maker(PRODUCT_MAKER)
                            .price(PRODUCT_PRICE)
                            .build();

                    given(productService.updateProduct(eq(EXISTED_PRODUCT_ID), any(ProductData.class)))
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
                }

                @Test
                @DisplayName("HttpStatus 200 OK를 응답한다")
                void it_returns_ok() throws Exception {
                    String content = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(patch("/products/" + EXISTED_PRODUCT_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                            .header(AUTHORIZATION, BEARER + VALID_TOKEN))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(UPDATED_PRODUCT_NAME)));

                    verify(productService).updateProduct(eq(EXISTED_PRODUCT_ID), any(ProductData.class));
                }
            }

            @Nested
            @DisplayName("만약 존재하지 않는 상품 id로 요청이 들어오면")
            class Context_not_existed_id {

                @BeforeEach
                void prepare() {
                    given(productService.updateProduct(eq(NOT_EXISTED_PRODUCT_ID), any(ProductData.class)))
                            .willThrow(new ProductNotFoundException(NOT_EXISTED_PRODUCT_ID));
                }

                @Test
                @DisplayName("HttpStatus 404 Not Found를 응답한다")
                void it_returns_not_found() throws Exception {
                    String content = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(patch("/products/" + NOT_EXISTED_PRODUCT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                            .header(AUTHORIZATION, BEARER + VALID_TOKEN))
                            .andExpect(status().isNotFound());

                    verify(productService).updateProduct(eq(NOT_EXISTED_PRODUCT_ID), any(ProductData.class));
                }
            }

            @Nested
            @DisplayName("만약 빈 상품 수정 정보로 요청이 들어오면")
            class Context_with_invalid_product_data {

                @BeforeEach
                void prepareCreate() {
                    productData = ProductData.builder()
                            .build();
                }

                @Test
                @DisplayName("HttpStatus 400 Bad Request를 응답한다")
                void it_returns_bad_request() throws Exception {
                    String content = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(patch("/products/" + EXISTED_PRODUCT_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                            .header(AUTHORIZATION, BEARER + VALID_TOKEN))
                            .andExpect(status().isBadRequest());
                }
            }
        }

        @Nested
        @DisplayName("DELETE /products/{id}")
        class Describe_destroy {

            @Nested
            @DisplayName("만약 존재하는 상품 id로 요청이 들어오면")
            class Context_with_existed_id {

                @Test
                @DisplayName("HttpStatus 200 OK를 응답한다")
                void it_returns_no_content() throws Exception {
                    mockMvc.perform(delete("/products/" + EXISTED_PRODUCT_ID)
                            .header(AUTHORIZATION, BEARER + VALID_TOKEN))
                            .andExpect(status().isOk());

                    verify(productService, atLeastOnce()).deleteProduct(EXISTED_PRODUCT_ID);
                }
            }

            @Nested
            @DisplayName("만약 존재하지 않는 상품 id로 요청이 들어오면")
            class Context_with_not_existed_id {

                @BeforeEach
                void prepare() {
                    given(productService.deleteProduct(NOT_EXISTED_PRODUCT_ID))
                            .willThrow(new ProductNotFoundException(NOT_EXISTED_PRODUCT_ID));
                }

                @Test
                @DisplayName("HttpStatus 404 Not Found를 응답한다")
                void it_returns_not_found() throws Exception {
                    mockMvc.perform(delete("/products/" + NOT_EXISTED_PRODUCT_ID)
                            .header(AUTHORIZATION, BEARER + VALID_TOKEN))
                            .andExpect(status().isNotFound());

                    verify(productService, atLeastOnce()).deleteProduct(NOT_EXISTED_PRODUCT_ID);
                }
            }
        }
    }

    @Nested
    @DisplayName("Jwt 토큰이 유효하지 않을때")
    class Context_with_invalid_jwt_token {

        @Nested
        @DisplayName("POST /products 는")
        class Describe_create {

            @Nested
            @DisplayName("상품 생성 요청이 들어오면")
            class Context_with_valid_product_data {

                @BeforeEach
                void prepare() {
                    given(authenticationService.parseToken(PREFIX + INVALID_TOKEN))
                            .willThrow(new InvalidTokenException(PREFIX + INVALID_TOKEN));
                }

                @Test
                @DisplayName("HttpStatus 401 Unauthorized를 응답한다")
                void it_returns_unauthorized() throws Exception {
                    String content = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(post("/products")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                            .header(AUTHORIZATION, BEARER + PREFIX + INVALID_TOKEN))
                            .andExpect(status().isUnauthorized());
                }
            }
        }

        @Nested
        @DisplayName("PATCH /products/{id} 는")
        class Describe_update {

            @Nested
            @DisplayName("상품 수정 요청이 들어오면")
            class Context_with_valid_product_data {

                @BeforeEach
                void prepare() {
                    given(authenticationService.parseToken(INVALID_TOKEN))
                            .willThrow(new InvalidTokenException(INVALID_TOKEN));
                }

                @Test
                @DisplayName("HttpStatus 401 Unauthorized를 응답한다")
                void it_returns_unauthorized() throws Exception {
                    String content = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(patch("/products/" + EXISTED_PRODUCT_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                            .header(AUTHORIZATION, BEARER + INVALID_TOKEN))
                            .andExpect(status().isUnauthorized());
                }
            }
        }

        @Nested
        @DisplayName("DELETE /products/{id}")
        class Describe_destroy {

            @Nested
            @DisplayName("상품 삭제 요청이 들어오면")
            class Context_with_existed_id {

                @BeforeEach
                void prepare() {
                    given(authenticationService.parseToken(INVALID_TOKEN + POSTFIX))
                            .willThrow(new InvalidTokenException(INVALID_TOKEN + POSTFIX));
                }

                @Test
                @DisplayName("HttpStatus 401 Unauthorized를 응답한다")
                void it_returns_unauthorized() throws Exception {
                    mockMvc.perform(delete("/products/" + EXISTED_PRODUCT_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(AUTHORIZATION, BEARER + INVALID_TOKEN + POSTFIX))
                            .andExpect(status().isUnauthorized());
                }
            }
        }
    }

    @Nested
    @DisplayName("Jwt 토큰이 없을때")
    class Context_with_not_existed_jwt_token {

        @Nested
        @DisplayName("POST /products 는")
        class Describe_create {

            @Nested
            @DisplayName("상품 생성 요청이 들어오면")
            class Context_with_valid_product_data {

                @Test
                @DisplayName("HttpStatus 401 Unauthorized를 응답한다")
                void it_returns_unauthorized() throws Exception {
                    String content = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(post("/products")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                            .andExpect(status().isUnauthorized());
                }
            }
        }

        @Nested
        @DisplayName("PATCH /products/{id} 는")
        class Describe_update {

            @Nested
            @DisplayName("상품 수정 요청이 들어오면")
            class Context_with_valid_product_data {

                @Test
                @DisplayName("HttpStatus 401 Unauthorized를 응답한다")
                void it_returns_unauthorized() throws Exception {
                    String content = objectMapper.writeValueAsString(productData);
                    mockMvc.perform(patch("/products/" + EXISTED_PRODUCT_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                            .andExpect(status().isUnauthorized());
                }
            }
        }

        @Nested
        @DisplayName("DELETE /products/{id} 는")
        class Describe_destroy {

            @Nested
            @DisplayName("상품 삭제 요청이 들어오면")
            class Context_with_valid_product_data {

                @Test
                @DisplayName("HttpStatus 401 Unauthorized를 응답한다")
                void it_returns_unauthorized() throws Exception {
                    mockMvc.perform(delete("/products/" + EXISTED_PRODUCT_ID)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isUnauthorized());
                }
            }
        }
    }
}
