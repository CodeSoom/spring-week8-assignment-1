package com.codesoom.assignment.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Price {
    private Integer price;

    public Price() {}

    public Price(Integer price) {
        validPrice(price);
        this.price = price;
    }

    private void validPrice(Integer price) {
        if (price == null || price < 0) {
            throw new IllegalArgumentException("가격은 음수이거나 null 일 수 없습니다.");
        }
    }
}
