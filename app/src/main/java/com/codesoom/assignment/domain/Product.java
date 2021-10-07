package com.codesoom.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 기존 상품의 아이디, 이름, 만든 곳, 가격, 상품 이미지의 주소를 담고 있습니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * 상품의 아이디
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 상품의 이름
     */
    private String name;

    /**
     * 상품의 제조사
     */
    private String maker;

    /**
     * 상품의 가격
     */
    private Integer price;

    /**
     * 상품의 이미지 주소
     */
    private String imageUrl;

    /**
     * 상품의 데이터를 변경해주는 생성자
     * @param source 상품 데이터
     */
    public void changeWith(Product source) {
        this.name = source.name;
        this.maker = source.maker;
        this.price = source.price;
    }
}
