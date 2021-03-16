package com.codesoom.assignment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/** 상품 조회에 사용한다. */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ProductUpdateData {
    @NotBlank(message = "name 값은 필수입니다")
    private String name;

    @NotBlank(message = "maker 값은 필수입니다")
    private String maker;

    @NotNull(message = "price 값은 필수입니다")
    private Integer price;

    private String imageUrl;

    @Builder
    public ProductUpdateData(String name, String maker, Integer price, String imageUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
