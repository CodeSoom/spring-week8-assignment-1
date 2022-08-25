package com.codesoom.assignment.domain;

import com.codesoom.assignment.dto.ProductData;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    private String name;

    private String description;

    @Embedded
    private Quantity quantity;

    @Embedded
    private Price price;

    public Product() {}

    public int getQuantity() {
        return quantity.getQuantity();
    }

    public int getPrice() {
        return price.getPrice();
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Builder
    public Product(User owner, String name, String description, int quantity, Integer price) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.quantity = new Quantity(quantity);
        this.price = new Price(price);
    }

    public Product update(ProductData productData) {
        this.name = productData.getName();
        this.description = productData.getDescription();
        this.quantity = new Quantity(productData.getQuantity());
        this.price = new Price(productData.getPrice());
        return this;
    }
}
