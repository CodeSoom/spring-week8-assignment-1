package com.codesoom.assignment.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Price price1 = (Price) o;

        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
