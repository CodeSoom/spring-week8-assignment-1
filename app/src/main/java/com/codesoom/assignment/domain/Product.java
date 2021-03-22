package com.codesoom.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 상품.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String maker;

    private Integer price;

    private String imageUrl;

    /**
     * 주어진 상품으로 현재 상품을 갱신합니다.
     * @param source 주어진 상품
     */
    public void changeWith(Product source) {
        this.name = source.name;
        this.maker = source.maker;
        this.price = source.price;
    }
}
