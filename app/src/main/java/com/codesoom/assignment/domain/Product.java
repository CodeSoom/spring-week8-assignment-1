package com.codesoom.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 제품 정보
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * 제품의 고유 ID값
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 제품의 이름
     */
    private String name;

    /**
     * 제품의 제조사
     */
    private String maker;

    /**
     * 제품의 가격
     */
    private Integer price;

    /**
     * 제품 사진의 주소
     */
    private String imageUrl;

    /**
     * 제품의 정보를 변경합니다.
     *
     * @param source 변경할 제품의 정보
     */
    public void changeWith(Product source) {
        this.name = source.name;
        this.maker = source.maker;
        this.price = source.price;
    }
}
