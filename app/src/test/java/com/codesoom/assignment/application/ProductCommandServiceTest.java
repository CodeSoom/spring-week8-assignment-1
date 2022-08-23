package com.codesoom.assignment.application;

import com.codesoom.assignment.Fixture;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.ProductInquiryInfo;
import com.codesoom.assignment.dto.UserInquiryInfo;
import com.codesoom.assignment.dto.UserRegisterData;
import com.codesoom.assignment.security.UserAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@DisplayName("ProductService 인터페이스의")
public class ProductCommandServiceTest {
    @Autowired
    private UserCommandService userCommandService;
    @Autowired
    private ProductCommandService productCommandService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("register 메서드는")
    class Describe_register {
        @Nested
        @DisplayName("상품, 유저 정보가 주어지면")
        class Context_with_productAndUserData {
            private ProductData productData;
            private Authentication authentication;

            @BeforeEach
            void prepare() {
                UserInquiryInfo info = userCommandService.register(
                        new UserRegisterData(Fixture.EMAIL, Fixture.PASSWORD, Fixture.USER_NAME));

                authentication = new UserAuthentication(info.getId(), info.getRole());

                productData = ProductData.builder()
                        .name(Fixture.PRODUCT_NAME)
                        .description(null)
                        .quantity(Fixture.QUANTITY)
                        .price(Fixture.PRICE)
                        .build();
            }

            @Test
            @DisplayName("상품을 생성하고 상품 조회 정보를 리턴한다")
            void It_returns_product() {
                ProductInquiryInfo product = productCommandService.register(productData, authentication);

                assertAll(
                        () -> assertThat(product.getName()).isEqualTo(Fixture.PRODUCT_NAME),
                        () -> assertThat(product.getDescription()).isNull(),
                        () -> assertThat(product.getQuantity()).isEqualTo(Fixture.QUANTITY),
                        () -> assertThat(product.getPrice()).isEqualTo(Fixture.PRICE)
                );
            }
        }
    }
}
