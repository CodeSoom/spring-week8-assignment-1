package com.codesoom.assignment.product.ui;


import com.codesoom.assignment.auth.application.AuthenticationService;
import com.codesoom.assignment.auth.application.InvalidTokenException;
import com.codesoom.assignment.common.RestDocConfiguration;
import com.codesoom.assignment.product.application.ProductNotFoundException;
import com.codesoom.assignment.product.application.ProductService;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.dto.ProductData;
import com.codesoom.assignment.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(RestDocConfiguration.class)
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
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(print())
                .andDo(document("get-products",
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("상품 식별자"),
                                fieldWithPath("[].name").type(STRING).description("상품 이름"),
                                fieldWithPath("[].maker").type(STRING).description("상품 제조사"),
                                fieldWithPath("[].price").type(NUMBER).description("상품 가격"),
                                fieldWithPath("[].imageUrl").type(STRING).optional().description("상품 이미지")
                        ))
                )

        ;
    }

    @Test
    void deatilWithExsitedProduct() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/products/{id}", 1L)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(print())
                .andDo(document("get-product",
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("상품 식별자"),
                                fieldWithPath("name").type(STRING).description("상품 이름"),
                                fieldWithPath("maker").type(STRING).description("상품 제조사"),
                                fieldWithPath("price").type(NUMBER).description("상품 가격"),
                                fieldWithPath("imageUrl").type(STRING).optional().description("상품 이미지")
                        ))
                );
    }

    @Test
    void deatilWithNotExsitedProduct() throws Exception {
        mockMvc.perform(get("/products/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWithValidAttributes() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.
                        post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(print())
                .andDo(document("create-product",
                        requestHeaders(headerWithName("Authorization").description("JWT 토큰")),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("상품 이름")
                                        .attributes(key("constraints").value("한 글자 이상 입력해야합니다.")),
                                fieldWithPath("maker").type(STRING).description("상품 제조사")
                                        .attributes(key("constraints").value("한 글자 이상 입력해야합니다.")),
                                fieldWithPath("price").type(NUMBER).description("상품 가격")
                                        .attributes(key("constraints").value("빈 값을 입력할 수 없습니다.")),
                                fieldWithPath("imageUrl").type(STRING).description("상품 이미지").optional()
                                        .attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("상품 식별자"),
                                fieldWithPath("name").type(STRING).description("상품 이름"),
                                fieldWithPath("maker").type(STRING).description("상품 제조사"),
                                fieldWithPath("price").type(NUMBER).description("상품 가격"),
                                fieldWithPath("imageUrl").type(STRING).optional().description("상품 이미지")
                        ))
                );

        verify(productService).createProduct(any(ProductData.class));
    }

    @Test
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"maker\":\"\"," +
                                "\"price\":0}")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithoutAccessToken() throws Exception {
        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithWrongAccessToken() throws Exception {
        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.
                        patch("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐순이")))
                .andDo(print())
                .andDo(document("update-product",
                        requestHeaders(headerWithName("Authorization").description("JWT 토큰")),
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("상품 이름")
                                        .attributes(key("constraints").value("한 글자 이상 입력해야합니다.")),
                                fieldWithPath("maker").type(STRING).description("상품 제조사")
                                        .attributes(key("constraints").value("한 글자 이상 입력해야합니다.")),
                                fieldWithPath("price").type(NUMBER).description("상품 가격")
                                        .attributes(key("constraints").value("빈 값을 입력할 수 없습니다.")),
                                fieldWithPath("imageUrl").type(STRING).description("상품 이미지").optional()
                                        .attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("상품 식별자"),
                                fieldWithPath("name").type(STRING).description("상품 이름"),
                                fieldWithPath("maker").type(STRING).description("상품 제조사"),
                                fieldWithPath("price").type(NUMBER).description("상품 가격"),
                                fieldWithPath("imageUrl").type(STRING).optional().description("상품 이미지")
                        ))
                );

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
                .andExpect(status().isNotFound());

        verify(productService).updateProduct(eq(1000L), any(ProductData.class));
    }

    @Test
    void updateWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"maker\":\"\"," +
                                "\"price\":0}")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithoutAccessToken() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateWithInvalidAccessToken() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void destroyWithExistedProduct() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.
                delete("/products/{id}", 1L)
                .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-product",
                        requestHeaders(headerWithName("Authorization").description("JWT 토큰")),
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")
                        )
                ));

        verify(productService).deleteProduct(1L);
    }

    @Test
    void destroyWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                delete("/products/1000")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(productService).deleteProduct(1000L);
    }

    @Test
    void destroyWithoutAccessToken() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void destroyWithInvalidAccessToken() throws Exception {
        mockMvc.perform(
                patch("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                "\"price\":5000}")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
        )
                .andExpect(status().isUnauthorized());
    }
}
