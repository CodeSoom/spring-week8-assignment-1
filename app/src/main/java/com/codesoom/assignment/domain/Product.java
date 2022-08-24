package com.codesoom.assignment.domain;

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

    private Integer price;

    public int getQuantity() {
        return quantity.getQuantity();
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Product() {}

    @Builder
    public Product(User owner, String name, String description, int quantity, Integer price) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.quantity = new Quantity(quantity);
        this.price = price;
    }
}
