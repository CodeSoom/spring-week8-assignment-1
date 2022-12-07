package com.codesoom.assignment.repository;

import com.codesoom.assignment.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.codesoom.assignment.support.ProductFixture.상품_1번;
import static com.codesoom.assignment.support.ProductFixture.상품_2번;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Product 유닛 테스트")
class ProductTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class builder_메서드는 {

        @Test
        @DisplayName("인스턴스를 생성하여 리턴한다")
        void creationWithBuilder() {
            Product product = Product.builder()
                    .id(상품_2번.아이디())
                    .name(상품_2번.이름())
                    .maker(상품_2번.메이커())
                    .price(상품_2번.가격())
                    .imageUrl(상품_2번.이미지_URL())
                    .build();

            assertThat(product.getId()).isEqualTo(상품_2번.아이디());
            assertThat(product.getName()).isEqualTo(상품_2번.이름());
            assertThat(product.getMaker()).isEqualTo(상품_2번.메이커());
            assertThat(product.getPrice()).isEqualTo(상품_2번.가격());
            assertThat(product.getImageUrl()).isEqualTo(상품_2번.이미지_URL());
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class update_메서드는 {
        @Test
        @DisplayName("id를 제외하고 null이 아닌 모든 값들을 수정한다")
        void update() {
            Product product = 상품_1번.엔티티_생성();

            product.update(Product.builder()
                    .name(상품_2번.이름())
                    .maker(상품_2번.메이커())
                    .price(null)
                    .imageUrl(상품_2번.이미지_URL())
                    .build());

            assertThat(product.getName()).isEqualTo(상품_2번.이름());
            assertThat(product.getMaker()).isEqualTo(상품_2번.메이커());
            assertThat(product.getPrice()).isEqualTo(상품_1번.가격());
            assertThat(product.getImageUrl()).isEqualTo(상품_2번.이미지_URL());
        }
    }
}
