package com.codesoom.assignment.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String description;

    private Integer quantity;

    private Integer price;

    public void setUser(User user) {
        this.user = user;
    }

    public Product() {}

    @Builder
    public Product(User user, String name, String description, Integer quantity, Integer price) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
}
