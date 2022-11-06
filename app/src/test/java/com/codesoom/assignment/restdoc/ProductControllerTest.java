package com.codesoom.assignment.restdoc;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.support.RestDocsControllerSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.codesoom.assignment.support.FieldsItem.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("상품 컨트롤러 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class ProductControllerTest extends RestDocsControllerSupport {

    @Test
    void 모든_상품_조회() throws Exception {
        given(productService.getProducts()).willReturn(getProducts());

        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                of("[]", ARRAY, "상품",
                                        of("id", NUMBER, "아이디"),
                                        of("name", STRING, "이름"),
                                        of("maker", STRING, "제조사"),
                                        of("price", NUMBER, "가격"),
                                        of("imageUrl", STRING, "이미지"))
                                        .build()
                        )
                ));
    }

    @Test
    void 상품_상세_조회() throws Exception {
        given(productService.getProduct(anyLong())).willReturn(getProduct());

        ResultActions result = mockMvc.perform(get("/products/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("id").description("조회할 상품 아이디")
                        ),
                        responseFields(
                                of("id", NUMBER, "상품 아이디").toField(),
                                of("name", STRING, "상품명").toField(),
                                of("maker", STRING, "상품 제조사").toField(),
                                of("price", NUMBER, "상품 가격").toField(),
                                of("imageUrl", STRING, "상품 이미지").toField()
                        )
                ));
    }

    @Test
    void 상품_생성() throws Exception {
        given(productService.createProduct(any(ProductData.class))).willReturn(getProduct());

        ResultActions result = mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getProductData()))
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("회원 식별 토큰")
                        ),
                        requestFields(
                                of("name", STRING, "상품명").toField(),
                                of("maker", STRING, "상품 제조사").toField(),
                                of("price", NUMBER, "상품 가격").toField(),
                                of("imageUrl", STRING, "상품 이미지").toField()
                        ),
                        responseFields(
                                of("id", NUMBER, "상품 아이디").toField(),
                                of("name", STRING, "상품명").toField(),
                                of("maker", STRING, "상품 제조사").toField(),
                                of("price", NUMBER, "상품 가격").toField(),
                                of("imageUrl", STRING, "상품 이미지").toField()
                        )
                ));
    }

    @Test
    void 상품_수정() throws Exception {
        given(productService.updateProduct(anyLong(), any(ProductData.class))).willReturn(getProduct());

        ResultActions result = mockMvc.perform(patch("/products/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getProductData())));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("회원 식별 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("수정할 상품 아이디")
                        ),
                        requestFields(
                                of("name", STRING, "수정할 상품명").toField(),
                                of("maker", STRING, "수정할 상품 제조사").toField(),
                                of("price", NUMBER, "수정할 상품 가격").toField(),
                                of("imageUrl", STRING, "수정할 상품 이미지").toField()
                        ),
                        responseFields(
                                of("id", NUMBER, "상품 아이디").toField(),
                                of("name", STRING, "상품명").toField(),
                                of("maker", STRING, "상품 제조사").toField(),
                                of("price", NUMBER, "상품 가격").toField(),
                                of("imageUrl", STRING, "상품 이미지").toField()
                        )
                ));
    }

    @Test
    void 상품_삭제() throws Exception {
        given(productService.deleteProduct(anyLong())).willReturn(getProduct());

        ResultActions result = mockMvc.perform(delete("/products/{id}", 1L)
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("회원 식별 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("삭제할 상품 아이디")
                        )
                ));
    }

    private List<Product> getProducts() {
        return List.of(
                Product.builder()
                        .id(1L)
                        .name("고양이 장난감")
                        .maker("코드숨")
                        .price(20000)
                        .imageUrl("cat-toy.jpg")
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("캣타워")
                        .maker("코드숨")
                        .price(50000)
                        .imageUrl("cat-tower.jpg")
                        .build()
        );
    }

    private Product getProduct() {
        return Product.builder()
                .id(1L)
                .name("고양이 장난감")
                .maker("코드숨")
                .price(20000)
                .imageUrl("cat-toy.jpg")
                .build();
    }

    private ProductData getProductData() {
        return ProductData.builder()
                .name("고양이 장난감")
                .maker("코드숨")
                .price(20000)
                .imageUrl("cat-toy.jpg")
                .build();
    }
}
