package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.controller.Item;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@DisplayName("ProductReadController 클래스")
public class ProductReadControllerMockMvcTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @DisplayName("getProducts 메서드는")
    @Nested
    class Describe_get_products {

        private final Product SAVED_PRODUCT
                = Product.withoutId("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "");

        @BeforeEach
        void setup() {
            repository.save(SAVED_PRODUCT);
        }

        @AfterEach
        void cleanup() {
            repository.deleteAll();
        }

        @DisplayName("저장된 모든 상품을 반환한다.")
        @Test
        void will_return_all_products() throws Exception {

            Item responseItems = Item.of("[]", JsonFieldType.ARRAY, "상품 목록 배열",
                    Item.of("id", NUMBER, "상품 식별자"),
                    Item.of("name", STRING, "상품명"),
                    Item.of("maker", STRING, "제조사"),
                    Item.of("price", NUMBER, "가격"),
                    Item.of("imageUrl", STRING, "상품 이미지 URL"));

            final MvcResult result = mockMvc.perform(get("/products"))
                    .andExpect(status().isOk())
                    .andDo(document("get-products-all"
                            , responseFields(responseItems.build())))
                    .andReturn();

            final List<Product> products
                    = objectMapper.readValue(result.getResponse().getContentAsByteArray(), List.class);
            assertThat(products).isNotEmpty();
        }
    }

    @DisplayName("getProduct 메서드는")
    @Nested
    class Describe_get_product {

        @DisplayName("찾을 수 있는 상품의 id가 주어지면")
        @Nested
        class Context_with_exist_id {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                final Product product
                        = Product.withoutId("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "");
                this.EXIST_ID = repository.save(product).getId();
            }

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("찾은 상품을 반환한다.")
            @Test
            void will_return_found_product() throws Exception {



                final MvcResult result = mockMvc.perform(get("/products/{id}", EXIST_ID))
                        .andExpect(status().isOk())
                        .andDo(document("get-products"
                                , pathParameters(
                                        parameterWithName("id").description("상품 식별자")
                                ), responseFields(
                                        Item.of("id", NUMBER, "상품 식별자").toField(),
                                        Item.of("name", STRING, "상품명").toField(),
                                        Item.of("maker", STRING, "제조사").toField(),
                                        Item.of("price", NUMBER, "가격").toField(),
                                        Item.of("imageUrl", STRING, "상품 이미지 URL").toField()
                                )))
                        .andReturn();
                final Product product = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Product.class);
                assertThat(product.getId()).isEqualTo(EXIST_ID);
            }
        }

        @DisplayName("찾을 수 없는 상품의 id가 주어지면")
        @Nested
        class Context_with_not_exist_id {

            private Long NOT_EXIST_ID = 100L;

            @BeforeEach
            void setup() {
                if (repository.existsById(NOT_EXIST_ID)) {
                    repository.deleteById(NOT_EXIST_ID);
                }
            }

            @DisplayName("404 not found를 응답한다.")
            @Test
            void will_response_404_not_found() throws Exception {
                mockMvc.perform(get("/products/{id}", NOT_EXIST_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }

}
