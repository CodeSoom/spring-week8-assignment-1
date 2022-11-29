package com.codesoom.assignment.application;

import com.codesoom.assignment.product.application.ProductService;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.domain.ProductRepository;
import com.codesoom.assignment.product.exception.ProductNotFoundException;
import com.codesoom.assignment.support.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.codesoom.assignment.support.IdFixture.ID_MAX;
import static com.codesoom.assignment.support.ProductFixture.상품_1번;
import static com.codesoom.assignment.support.ProductFixture.상품_2번;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("ProductService 유닛 테스트")
class ProductServiceTest {
    private static final Long 찾을_수_있는_id = 상품_1번.아이디();
    private static final Long 찾을_수_없는_id = ID_MAX.value();
    private ProductService productService;
    private final ProductRepository productRepository = mock(ProductRepository.class);

    @BeforeEach
    void setUpVariable() {
        productService = new ProductService(productRepository);
    }

    @BeforeEach
    void setUp() {
        given(productRepository.findById(찾을_수_있는_id))
                .willReturn(
                        Optional.of(상품_1번.엔티티_생성(찾을_수_있는_id))
                );

        given(productRepository.findById(찾을_수_없는_id))
                .willReturn(Optional.empty());
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getProducts_메서드는 {

        @Nested
        @DisplayName("등록된 장난감이 없을 때")
        class Context_with_not_exist_product {
            @BeforeEach
            void setUp() {
                given(productRepository.findAll()).willReturn(List.of());
            }

            @Test
            @DisplayName("빈 리스트를 리턴한다")
            void it_returns_empty_list() {
                assertThat(productService.getProducts()).isEmpty();

                verify(productRepository).findAll();
            }
        }

        @Nested
        @DisplayName("등록된 장난감이 있을 때")
        class Context_with_exist_product {
            @BeforeEach
            void setUp() {
                given(productRepository.findAll())
                        .willReturn(
                                List.of(상품_1번.엔티티_생성(상품_1번.아이디()))
                        );
            }

            @Test
            @DisplayName("장난감 목록을 리턴한다")
            void it_returns_product_list() {
                List<Product> products = productService.getProducts();

                assertThat(products).isNotEmpty();
                PRODUCT_이름_메이커_가격_이미지주소_값_검증(products.get(0), 상품_1번);

                verify(productRepository).findAll();
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getProduct_메서드는 {

        @Nested
        @DisplayName("찾을 수 없는 id가 주어지면")
        class Context_with_not_exist_id {

            @Test
            @DisplayName("ProductNotFoundException 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> productService.getProduct(찾을_수_없는_id))
                        .isInstanceOf(ProductNotFoundException.class);

                verify(productRepository).findById(찾을_수_없는_id);
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id가 주어지면")
        class Context_with_exist_id {

            @Test
            @DisplayName("해당 id의 장난감 정보를 리턴한다")
            void it_returns_product() {
                Product product = productService.getProduct(찾을_수_있는_id);

                assertThat(product.getId()).isEqualTo(찾을_수_있는_id);
                PRODUCT_이름_메이커_가격_이미지주소_값_검증(product, 상품_1번);

                verify(productRepository).findById(찾을_수_있는_id);
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class createProduct_메서드는 {

        @Nested
        @DisplayName("유효한 상품 정보가 주어지면")
        class Context_with_valid_product {
            @BeforeEach
            void setUp() {
                given(productRepository.save(eq(상품_2번.엔티티_생성())))
                        .will(invocation -> {
                            Product product = invocation.getArgument(0);

                            return Product.builder()
                                    .id(상품_2번.아이디())
                                    .name(product.getName())
                                    .maker(product.getMaker())
                                    .price(product.getPrice())
                                    .imageUrl(product.getImageUrl())
                                    .build();
                        });
            }

            @Test
            @DisplayName("상품을 저장하고 리턴한다")
            void it_returns_product() {
                Product product = productService.createProduct(상품_2번.등록_요청_데이터_생성());

                assertThat(product.getId()).isEqualTo(상품_2번.아이디());
                PRODUCT_이름_메이커_가격_이미지주소_값_검증(product, 상품_2번);

                verify(productRepository).save(상품_2번.엔티티_생성());
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class updateProduct_메서드는 {

        @Nested
        @DisplayName("찾을 수 없는 id가 주어지면")
        class Context_with_not_exist_id {

            @Test
            @DisplayName("ProductNotFoundException 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(
                        () -> productService.updateProduct(찾을_수_없는_id, 상품_1번.수정_요청_데이터_생성())
                )
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id가 주어지면")
        class Context_with_exist_id {

            @Test
            @DisplayName("상품을 수정하고 리턴한다")
            void it_returns_product() {
                Product product = productService.updateProduct(찾을_수_있는_id, 상품_2번.수정_요청_데이터_생성());

                PRODUCT_이름_메이커_가격_이미지주소_값_검증(product, 상품_2번);

                verify(productRepository).findById(찾을_수_있는_id);
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class deleteProduct_메서드는 {
        @Nested
        @DisplayName("찾을 수 없는 id가 주어지면")
        class Context_with_not_exist_id {

            @Test
            @DisplayName("ProductNotFoundException 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> productService.deleteProduct(찾을_수_없는_id))
                        .isInstanceOf(ProductNotFoundException.class);

                verify(productRepository, never()).delete(any(Product.class));
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id가 주어지면")
        class Context_with_exist_id {

            @Test
            @DisplayName("상품을 삭제하고 리턴한다")
            void it_deleted() {
                productService.deleteProduct(찾을_수_있는_id);

                verify(productRepository).delete(상품_1번.엔티티_생성(찾을_수_있는_id));
            }
        }
    }


    private void PRODUCT_이름_메이커_가격_이미지주소_값_검증(final Product product,
                                              final ProductFixture productFixture) {
        assertThat(product.getName()).isEqualTo(productFixture.이름());
        assertThat(product.getMaker()).isEqualTo(productFixture.메이커());
        assertThat(product.getPrice()).isEqualTo(productFixture.가격());
        assertThat(product.getImageUrl()).isEqualTo(productFixture.이미지_URL());
    }
}
