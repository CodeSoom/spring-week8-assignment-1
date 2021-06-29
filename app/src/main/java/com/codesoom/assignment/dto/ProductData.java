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
 * 제품정보 DTO
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {
    /**
     * 제품의 고유 ID값
     */
    private Long id;

    /**
     * 제품의 이름
     */
    @NotBlank
    @Mapping("name")
    private String name;

    /**
     * 제품의 제조사
     */
    @NotBlank
    @Mapping("maker")
    private String maker;

    /**
     * 제품의 가격
     */
    @NotNull
    @Mapping("price")
    private Integer price;

    /**
     * 제품 사진의 주소
     */
    @Mapping("imageUrl")
    private String imageUrl;
}
