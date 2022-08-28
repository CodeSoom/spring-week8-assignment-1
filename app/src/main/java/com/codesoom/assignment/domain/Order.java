package com.codesoom.assignment.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected Order() {
    }

    public Order(User buyer, Product product, int quantity) {
        throwIfBuyerAndSellerIsSame(buyer, product);
        throwIfPurchaseQuantityGreaterThanProductStock(product, quantity);
        this.buyer = buyer;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public Order(Long id, User buyer, Product product, int quantity) {
        throwIfBuyerAndSellerIsSame(buyer, product);
        throwIfPurchaseQuantityGreaterThanProductStock(product, quantity);
        this.id = id;
        this.buyer = buyer;
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

    public static class IllegalBuyer extends RuntimeException {
    }

    public static class IllegalPurchaseQuantity extends RuntimeException {
    }
}
