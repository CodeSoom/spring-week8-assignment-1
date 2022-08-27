package com.codesoom.assignment.domain;

import com.codesoom.assignment.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    @Test
    @DisplayName("판매자는 자신이 파는 상품을 구매할 수 없다.")
    void sellerCannotBuyTheGoodsHeSells() {
        // given
        User seller = User.builder()
                .id(1L)
                .email(Fixture.ADMIN_EMAIL)
                .password(Fixture.PASSWORD)
                .name(Fixture.ADMIN_NAME)
                .build();

        Product product = Fixture.makeProduct(seller);
        // when, then
        assertThatThrownBy(() -> new Order(1L, seller, product, 7))
                .isExactlyInstanceOf(Order.IllegalBuyer.class);
    }

    @Test
    @DisplayName("구매 수량은 상품의 재고 수량보다 많을 수 없다.")
    void purchaseQuantityCannotBeGreaterThanStockQuantity() {
        // given
        User seller = User.builder()
                .id(1L)
                .email(Fixture.ADMIN_EMAIL)
                .password(Fixture.PASSWORD)
                .name(Fixture.ADMIN_NAME)
                .build();

        User buyer = User.builder()
                .id(2L)
                .email(Fixture.EMAIL)
                .password(Fixture.PASSWORD)
                .name(Fixture.USER_NAME)
                .build();

        Product product = Product.builder()
                .owner(seller)
                .name(Fixture.PRODUCT_NAME)
                .quantity(10)
                .price(Fixture.PRICE)
                .build();
        // when, then
        assertThatThrownBy(() -> new Order(1L, buyer, product, 11))
                .isExactlyInstanceOf(Order.IllegalPurchaseQuantity.class);
    }

    static class Order {
        private Long id;
        private User seller;
        private Product product;
        private Quantity quantity;

        public Order(Long id, User seller, Product product, int quantity) {
            throwIfBuyerAndSellerIsSame(seller, product);
            throwIfPurchaseQuantityGreaterThanProductStock(product, quantity);
            this.id = id;
            this.seller = seller;
            this.product = product;
            this.quantity = new Quantity(quantity);
        }

        private void throwIfPurchaseQuantityGreaterThanProductStock(Product product, int quantity) {
            if (product.getQuantity() < quantity) {
                throw new IllegalPurchaseQuantity();
            }
        }

        private void throwIfBuyerAndSellerIsSame(User seller, Product product) {
            if (seller.isSameUser(product.getOwner().getId())) {
                throw new IllegalBuyer();
            }
        }

        private static class IllegalBuyer extends RuntimeException {
        }

        public static class IllegalPurchaseQuantity extends RuntimeException {
        }
    }
}
