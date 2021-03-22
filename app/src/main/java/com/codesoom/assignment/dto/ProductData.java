package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 상품 DTO.
 */
@Getter
@NoArgsConstructor
public class ProductData {
    private Long id;

    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Mapping("maker")
    private String maker;

    @NotNull
    @Mapping("price")
    private Integer price;

    @Mapping("image")
    private String image;

    @Builder
    public ProductData(
            String name, String maker, Integer price, String image
    ) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.image = image;
    }
}
