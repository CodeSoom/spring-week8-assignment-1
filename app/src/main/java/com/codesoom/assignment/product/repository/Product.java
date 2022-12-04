package com.codesoom.assignment.product.repository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String maker;

    private Integer price;

    private String imageUrl;

    @Builder
    public Product(Long id, String name, String maker, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    /**
     * 상품 정보를 수정합니다. <br>
     * 필드가 null일 경우 수정하지 않습니다.
     *
     * @param updateProduct 수정할 상품 정보
     */
    public final void update(Product updateProduct) {
        updateName(updateProduct.getName());
        updateMaker(updateProduct.getMaker());
        updatePrice(updateProduct.getPrice());
        updateImageUrl(updateProduct.getImageUrl());
    }

    private void updateName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    private void updateMaker(String maker) {
        if (maker != null) {
            this.maker = maker;
        }
    }

    private void updatePrice(Integer price) {
        if (price != null) {
            this.price = price;
        }
    }

    private void updateImageUrl(String imageUrl) {
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }
}
