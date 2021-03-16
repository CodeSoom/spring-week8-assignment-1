package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/** 상품 생성에 사용한다. */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ProductCreateData {
    @NotBlank(message = "name 값은 필수입니다")
    private String name;

    @NotBlank(message = "maker 값은 필수입니다")
    private String maker;

    @NotNull(message = "price 값은 필수입니다")
    private Integer price;

    private String imageUrl;

    @Builder
    public ProductCreateData(String name, String maker, Integer price, String imageUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    /** 상품 데이터를 엔티티로 바꾼다. */
    public Product toEntity() {
        return Product.builder()
                .name(this.name)
                .maker(this.maker)
                .price(this.price)
                .imageUrl((this.imageUrl))
                .build();
    }
}
