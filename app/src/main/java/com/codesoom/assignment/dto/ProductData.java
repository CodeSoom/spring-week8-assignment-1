package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 등록하려는 상품 데이터의 아이디, 이름, 만든 곳, 가격, 이미지 주소를 담고 있습니다.
 */

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {
    /**
     * 등록하려는 상품의 아이디
     */
    private Long id;

    /**
     * 등록하려는 상품의 이름
     */
    @NotBlank
    @Mapping("name")
    private String name;

    /**
     * 등록하려는 상품의 제조사
     */
    @NotBlank
    @Mapping("maker")
    private String maker;

    /**
     * 등록하려는 상품의 가격
     */
    @NotNull
    @Mapping("price")
    private Integer price;

    /**
     * 등록하려는 상품의 이미지 주소
     */
    @Mapping("imageUrl")
    private String imageUrl;
}
