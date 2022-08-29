package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Order;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.OrderData;
import com.codesoom.assignment.dto.ProductInquiryInfo;
import com.codesoom.assignment.security.UserAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import static com.codesoom.assignment.Fixture.ADMIN_REGISTER_DATA;
import static com.codesoom.assignment.Fixture.PRODUCT_DATA;
import static com.codesoom.assignment.Fixture.USER_REGISTER_DATA;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private UserCommandService userCommandService;
    @Autowired
    private ProductCommandService productCommandService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductQueryService productQueryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("인가, 주문 정보가 주어지면 주문을 생성하고 상품의 수량을 줄인다")
    void givenOrderInfoAndAuthorizationThenCreatesOrderAndReducesQuantityOfGoods() {
        // given
        final User seller = userCommandService.register(USER_REGISTER_DATA);
        final Authentication sellerAuth = new UserAuthentication(seller.getId(), seller.getRole());
        final ProductInquiryInfo productInfo = productCommandService.register(PRODUCT_DATA, sellerAuth);

        final User buyer = userCommandService.register(ADMIN_REGISTER_DATA);
        final Authentication buyerAuth = new UserAuthentication(buyer.getId(), buyer.getRole());

        final OrderData orderData = new OrderData(productInfo.getId(), productInfo.getQuantity() - 1);
        // when
        final Order order = orderService.create(orderData, buyerAuth);
        // then
        assertThat(productQueryService.findById(productInfo.getId()).getQuantity()).isEqualTo(1);
    }
}
