package com.codesoom.assignment.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * 상품의 수량을 가지고 있습니다.
 */
@Getter
@Embeddable
public class Quantity {
    private int quantity;

    public Quantity(int quantity) {
        validQuantity(quantity);
        this.quantity = quantity;
    }

    public Quantity() {
    }

    private void validQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 음수일 수 없습니다.");
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

        Quantity quantity1 = (Quantity) o;

        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    public void reduceQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
