package com.codesoom.assignment.application.products;

import com.codesoom.assignment.application.ServiceTest;
import com.codesoom.assignment.exceptions.ProductNotFoundException;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductDto;
import com.codesoom.assignment.domain.products.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("ProductCommandService 클래스")
public class ProductCommandServiceTest extends ServiceTest {

    private static final String NAME = "쥐돌이";
    private static final String MAKER = "냥이월드";
    private static final BigDecimal PRICE = BigDecimal.valueOf(3000);
    private static final String IMG_URL = "fakeURL";
    private static final Product PRODUCT = Product.withoutId(NAME, MAKER, PRICE, IMG_URL);

    private ProductCommandService service;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        this.service = new ProductCommandService(repository);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    private ProductSaveRequest createProductSaveRequest(String name, String maker,
                                                        BigDecimal price, String imageUrl) {
        return  new ProductSaveRequest() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getMaker() {
                return maker;
            }

            @Override
            public BigDecimal getPrice() {
                return price;
            }

            @Override
            public String getImageUrl() {
                return imageUrl;
            }
        };
    }

    @DisplayName("saveProduct 메서드는")
    @Nested
    class Describe_save_product {

        private ProductSaveRequest PRODUCT_SAVE_REQUEST = createProductSaveRequest(NAME, MAKER, PRICE, IMG_URL);

        @DisplayName("상품을 등록하고, 등록된 상품을 반환한다.")
        @Test
        void will_return_saved_product() {
            final Product product = service.saveProduct(PRODUCT_SAVE_REQUEST);

            assertThat(repository.findById(product.getId())).isNotEmpty();
        }
    }

    @DisplayName("updateProduct 메서드는")
    @Nested
    class Describe_update {

        private String NEW_NAME = "쥐돌이 리뉴얼 버전";
        private ProductSaveRequest PRODUCT_SAVE_REQUEST_TO_UPDATE
                = createProductSaveRequest(NEW_NAME, MAKER, PRICE, IMG_URL);

        @DisplayName("존재하는 상품 id와 변경 데이터가 주어진다면")
        @Nested
        class Context_with_exist_id {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                final Product product = repository.save(PRODUCT);
                this.EXIST_ID = product.getId();
            }

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("상품을 성공적으로 변경한 뒤 변경 결과를 반환한다.")
            @Test
            void will_return_updated_product() {
                final Product product = service.updateProduct(EXIST_ID, PRODUCT_SAVE_REQUEST_TO_UPDATE);

                assertThat(product.getId()).isEqualTo(EXIST_ID);
                assertThat(product.getName()).isNotEqualTo(PRODUCT.getName());
            }
        }

        @DisplayName("존재하지 않는 상품 id와 변경 데이터가 주어진다면")
        @Nested
        class Context_with_not_exist_id {

            private final Long NOT_EXIST_ID = 100L;

            @BeforeEach
            void setup() {
                if (repository.existsById(NOT_EXIST_ID)) {
                    repository.deleteById(NOT_EXIST_ID);
                }
            }

            @DisplayName("예외를 던진다.")
            @Test
            void will_throw_exception() {
                assertThatThrownBy(() -> service.updateProduct(NOT_EXIST_ID, PRODUCT_SAVE_REQUEST_TO_UPDATE))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }

    @DisplayName("deleteProduct 메서드는")
    @Nested
    class Describe_delete_by_id {

        @DisplayName("존재하는 상품 id가 주어진다면")
        @Nested
        class Context_with_exist_id {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                this.EXIST_ID = repository.save(PRODUCT).getId();
            }

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("성공적으로 상품을 삭제한다.")
            @Test
            void will_delete_product() {
                service.deleteProduct(EXIST_ID);
                assertThat(repository.findById(EXIST_ID)).isEmpty();
            }
        }

        @DisplayName("존재하지 않는 상품 id가 주어진다면")
        @Nested
        class Context_with_not_exist_id {

            private final Long NOT_EXIST_ID = 100L;

            @BeforeEach
            void setup() {
                if (repository.existsById(NOT_EXIST_ID)) {
                    repository.deleteById(NOT_EXIST_ID);
                }
            }

            @DisplayName("예외를 던진다.")
            @Test
            void will_throw_exception() {
                assertThatThrownBy(() -> service.deleteProduct(NOT_EXIST_ID))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }

}
