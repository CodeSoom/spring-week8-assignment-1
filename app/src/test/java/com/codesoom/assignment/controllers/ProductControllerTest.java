package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.dto.ProductCreateData;
import com.codesoom.assignment.dto.ProductResultData;
import com.codesoom.assignment.dto.ProductUpdateData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import org.hamcrest.core.StringContains;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureRestDocs
@DisplayName("ProductController 테스트")
class ProductControllerTest {
    private static final String EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJleGlzdGVkRW1haWwifQ.UQodS3elf3Cu4g0PDFHqVloFbcKHHmTTnk0jGmiwPXY";
    private static final String NOT_EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJleGlzdGVkRW1haWwifQ.UQodS3elf3Cu4g0PDFHqVloFbcKHHmTTnk0jGmiwPXy";

    private static final String SETUP_PRODUCT_NAME = "setupName";
    private static final String SETUP_PRODUCT_MAKER = "setupMaker";
    private static final Integer SETUP_PRODUCT_PRICE = 100;
    private static final String SETUP_PRODUCT_IMAGEURL = "setupImage";

    private static final String CREATED_PRODUCT_NAME = "createdName";
    private static final String CREATED_PRODUCT_MAKER = "createdMaker";
    private static final Integer CREATED_PRODUCT_PRICE = 200;
    private static final String CREATED_PRODUCT_IMAGEURL = "createdImage";

    private static final String UPDATED_PRODUCT_NAME = "updatedName";
    private static final String UPDATED_PRODUCT_MAKER = "updatedMaker";
    private static final Integer UPDATED_PRODUCT_PRICE = 300;
    private static final String UPDATED_PRODUCT_IMAGEURL = "updatedImage";

    private static final Long EXISTED_ID = 1L;
    private static final Long CREATED_ID = 2L;
    private static final Long NOT_EXISTED_ID = 100L;
    private static final String EXISTED_EMAIL = "existedEmail";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    private List<Product> products;
    private Product setupProductOne;
    private Product setupProductTwo;
    private Product updateProduct;

    private List<ProductResultData> resultProducts;
    private ProductResultData resultProductOne;
    private ProductResultData resultProductTwo;
    private ProductResultData resultUpdateProduct;

    @BeforeEach
    void setUp() {
        setupProductOne = Product.builder()
                .id(EXISTED_ID)
                .name(SETUP_PRODUCT_NAME)
                .maker(SETUP_PRODUCT_MAKER)
                .price(SETUP_PRODUCT_PRICE)
                .imageUrl(SETUP_PRODUCT_IMAGEURL)
                .build();

        setupProductTwo = Product.builder()
                .id(CREATED_ID)
                .name(CREATED_PRODUCT_NAME)
                .maker(CREATED_PRODUCT_MAKER)
                .price(CREATED_PRODUCT_PRICE)
                .imageUrl(CREATED_PRODUCT_IMAGEURL)
                .build();

        updateProduct = Product.builder()
                .name(UPDATED_PRODUCT_NAME)
                .maker(UPDATED_PRODUCT_MAKER)
                .price(UPDATED_PRODUCT_PRICE)
                .imageUrl(UPDATED_PRODUCT_IMAGEURL)
                .build();

        products = Arrays.asList(setupProductOne, setupProductTwo);

        resultProductOne = ProductResultData.of(setupProductOne);
        resultProductTwo = ProductResultData.of(setupProductTwo);
        resultUpdateProduct = ProductResultData.of(updateProduct);
        resultProducts = Arrays.asList(resultProductOne, resultProductTwo);

        given(productService.getProducts()).willReturn(resultProducts);

        given(productService.getProduct(EXISTED_ID)).willReturn(setupProductOne);

        given(productService.getProduct(NOT_EXISTED_ID))
                .willThrow(new ProductNotFoundException(NOT_EXISTED_ID));

        given(authenticationService.parseToken(EXISTED_TOKEN)).willReturn(EXISTED_EMAIL);
        given(authenticationService.roles(EXISTED_EMAIL)).willReturn(Arrays.asList(new Role("USER")));
        given(productService.createProduct(any(ProductCreateData.class)))
                .will(invocation -> {
                    ProductCreateData productCreateData = invocation.getArgument(0);
                    return ProductResultData.builder()
                            .id(CREATED_ID)
                            .name(productCreateData.getName())
                            .maker(productCreateData.getMaker())
                            .price(productCreateData.getPrice())
                            .imageUrl(productCreateData.getImageUrl())
                            .build();
                });

        given(authenticationService.parseToken(eq(NOT_EXISTED_TOKEN)))
                .willThrow(new InvalidTokenException(NOT_EXISTED_TOKEN));

        given(productService.updateProduct(eq(EXISTED_ID), any(ProductUpdateData.class)))
                .will(invocation -> {
                    Long givenExistedId = invocation.getArgument(0);
                    ProductUpdateData productUpdateData = invocation.getArgument(1);
                    return ProductResultData.builder()
                            .id(givenExistedId)
                            .name(productUpdateData.getName())
                            .maker(productUpdateData.getMaker())
                            .price(productUpdateData.getPrice())
                            .imageUrl(resultProductOne.getImageUrl())
                            .build();
                });

        given(productService.updateProduct(eq(NOT_EXISTED_ID), any(ProductUpdateData.class)))
                .willThrow(new ProductNotFoundException(NOT_EXISTED_ID));

        given(authenticationService.parseToken(eq(NOT_EXISTED_TOKEN)))
                .willThrow(new InvalidTokenException(NOT_EXISTED_TOKEN));

        given(productService.deleteProduct(EXISTED_ID)).willReturn(resultProductOne);

        given(productService.deleteProduct(NOT_EXISTED_ID))
                .willThrow(new ProductNotFoundException(NOT_EXISTED_ID));
    }

    @Test
    void lists() throws Exception {
        mockMvc.perform(
                get("/products")
        )
                .andExpect(content().string(StringContains.containsString("\"id\":" + EXISTED_ID)))
                .andExpect(content().string(StringContains.containsString("\"id\":" + CREATED_ID)))
                .andExpect(status().isOk())
                .andDo(document("get-products",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("상품 식별자"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("[].maker").type(JsonFieldType.STRING).description("상품 제조사"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("상품 이미지").optional()
                        )
                ));

        verify(productService).getProducts();
    }

    @Test
    void detailWithExistedId() throws Exception {
        Long givenExistedId = EXISTED_ID;

        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/products/{id}", givenExistedId)
        )
                .andDo(print())
                .andExpect(jsonPath("id").value(givenExistedId))
                .andExpect(status().isOk())
                .andDo(document("get-product",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("조회하고자 하는 상품의 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 식별자"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("maker").type(JsonFieldType.STRING).description("상품 제조사"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지").optional()
                        )
                ));

        verify(productService).getProduct(givenExistedId);
    }

    @Test
    void detailWithNotExistedId() throws Exception {
        Long givenNotExistedId = NOT_EXISTED_ID;

        mockMvc.perform(
                get("/products/"+ givenNotExistedId)
        )
                .andDo(print())
                .andExpect(content().string(containsString("Product not found")))
                .andExpect(status().isNotFound());

        verify(productService).getProduct(givenNotExistedId);
    }

    @Test
    void createWithValidProduct() throws Exception {

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + EXISTED_TOKEN)
                .content("{\"name\":\"createdName\" , \"maker\":\"createdMaker\", \"price\":200, \"imageUrl\":\"createdImage\"}")
        )
                .andDo(print())
                .andExpect(content().string(containsString("\"id\":" + resultProductTwo.getId())))
                .andExpect(content().string(containsString("name\":\"" + resultProductTwo.getName())))
                .andExpect(content().string(containsString("\"maker\":\"" + resultProductTwo.getMaker())))
                .andExpect(content().string(containsString("\"price\":" + resultProductTwo.getPrice())))
                .andExpect(content().string(containsString("\"imageUrl\":\"" + resultProductTwo.getImageUrl())))
                .andExpect(status().isCreated())
                .andDo(document("create-product",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("maker").type(JsonFieldType.STRING).description("상품 제조사"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 식별자"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("maker").type(JsonFieldType.STRING).description("상품 제조사"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지").optional()
                        )
                ));

        verify(productService).createProduct(any(ProductCreateData.class));
    }

    @Test
    void createWithoutName() throws Exception {
        mockMvc.perform(
                post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + EXISTED_TOKEN)
                    .content("{\"name\":\"\" , \"maker\":\"createdMaker\", \"price\":200, \"imageUrl\":\"createdImage\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithoutMaker() throws Exception {
        mockMvc.perform(
                post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + EXISTED_TOKEN)
                    .content("{\"name\":\"createdName\" , \"maker\":\"\", \"price\":200, \"imageUrl\":\"createdImage\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithoutPrice() throws Exception {
        mockMvc.perform(
                post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + EXISTED_TOKEN)
                    .content("{\"name\":\"createdName\" , \"maker\":\"createdMaker\", \"price\": null, \"imageUrl\":\"createdImage\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithInvalidToken() throws Exception {
        final String givenNotExistedToken = NOT_EXISTED_TOKEN;

        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + givenNotExistedToken)
                        .content("{\"name\":\"createdName\" , \"maker\":\"createdMaker\", \"price\":200, \"imageUrl\":\"createdImage\"}")
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateWithExistedIdAndProduct() throws Exception {
        Long givenExistedId = EXISTED_ID;

        mockMvc.perform(
                patch("/products/" + givenExistedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + EXISTED_TOKEN)
                    .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\":300, \"imageUrl\":\"updatedImage\"}")
        )
                .andDo(print())
                .andExpect(jsonPath("name").value(resultUpdateProduct.getName()))
                .andExpect(jsonPath("maker").value(resultUpdateProduct.getMaker()))
                .andExpect(jsonPath("price").value(resultUpdateProduct.getPrice()))
                .andExpect(jsonPath("imageUrl").value(resultProductOne.getImageUrl()))
                .andExpect(status().isOk())
                .andDo(document("update-product",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                            fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                            fieldWithPath("maker").type(JsonFieldType.STRING).description("상품 제조사"),
                            fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지").optional()
                    ),
                    responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 식별자"),
                            fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                            fieldWithPath("maker").type(JsonFieldType.STRING).description("상품 제조사"),
                            fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지").optional()
                    )
        ));

        verify(productService).updateProduct(eq(givenExistedId), any(ProductUpdateData.class));
    }

    @Test
    void updateWithNotExistedId() throws Exception {
        Long givenNotExistedId = NOT_EXISTED_ID;

        mockMvc.perform(
                patch("/products/" + givenNotExistedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + EXISTED_TOKEN)
                    .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\":300, \"imageUrl\":\"updatedImage\"}")
        )
                .andDo(print())
                .andExpect(content().string(containsString("Product not found")))
                .andExpect(status().isNotFound());

        verify(productService).updateProduct(eq(givenNotExistedId), any(ProductUpdateData.class));
    }

    @Test
    void updateWithoutName() throws Exception {
        Long givenExistedId = EXISTED_ID;

        mockMvc.perform(
                patch("/products/" + givenExistedId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + EXISTED_TOKEN)
                .content("{\"name\":\"\" , \"maker\":\"updatedMaker\", \"price\":300, \"imageUrl\":\"updatedImage\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithoutMaker() throws Exception {
        Long givenExistedId = EXISTED_ID;

        mockMvc.perform(
                patch("/products/" + givenExistedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + EXISTED_TOKEN)
                    .content("{\"name\":\"updatedName\" , \"maker\":\"\", \"price\":300, \"imageUrl\":\"updatedImage\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithoutPrice() throws Exception {
        Long givenExistedId = EXISTED_ID;

        mockMvc.perform(
                patch("/products/" + givenExistedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + EXISTED_TOKEN)
                    .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\": null, \"imageUrl\":\"updatedImage\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithInvalidToken() throws Exception {
        Long givenExistedId = EXISTED_ID;

        mockMvc.perform(
                patch("/products/" + givenExistedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + NOT_EXISTED_TOKEN)
                    .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\":300, \"imageUrl\":\"updatedImage\"}")
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void DeleteWithExistedId() throws Exception {
        Long givenExistedId = EXISTED_ID;

        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/products/{id}", givenExistedId)
                    .header("Authorization", "Bearer " + EXISTED_TOKEN)
        )
                .andDo(print())
                .andExpect(jsonPath("id").value(givenExistedId))
                .andExpect(status().isNoContent())
                .andDo(document("delete-product",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("삭제하고자 하는 상품의 식별자")
                        ),
                    responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 식별자"),
                            fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                            fieldWithPath("maker").type(JsonFieldType.STRING).description("상품 제조사"),
                            fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지").optional()
                    )
        ));

        verify(productService).deleteProduct(givenExistedId);
    }

    @Test
    void DeleteWithNotExistedId() throws Exception {
        Long givenNotExistedId = NOT_EXISTED_ID;

        mockMvc.perform(
                delete("/products/" + givenNotExistedId)
                    .header("Authorization", "Bearer " + EXISTED_TOKEN)
        )
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(productService).deleteProduct(givenNotExistedId);
    }

    @Test
    void DeleteWithInvalidToken() throws Exception {
        Long givenExistedId = EXISTED_ID;
        String givenNotExistedToken = NOT_EXISTED_TOKEN;

        mockMvc.perform(
                delete("/products/" + givenExistedId)
                    .header("Authorization", "Bearer " + givenNotExistedToken)
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
