package com.codesoom.assignment.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuantityTest {
    @Test
    void quantityCannotBeNegative() {
        assertThatThrownBy(() -> new Quantity(-1))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
