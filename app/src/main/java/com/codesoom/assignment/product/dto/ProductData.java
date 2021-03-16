package com.codesoom.assignment.product.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 상품 명세서.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {
    /** 상품 식별자. */
    private Long id;

    /** 상품명. */
    @NotBlank
    @Mapping("name")
    private String name;

    /** 상품제조사. */
    @NotBlank
    @Mapping("maker")
    private String maker;

    /** 상품가격. */
    @NotNull
    @Mapping("price")
    private Integer price;

    /** 상품 이미지. */
    @Mapping("imageUrl")
    private String imageUrl;
}
