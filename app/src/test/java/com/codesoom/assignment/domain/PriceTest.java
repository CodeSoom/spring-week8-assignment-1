package com.codesoom.assignment.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriceTest {
    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -123892312})
    @NullSource
    void priceCannotBeNegativeOrNull(int price) {
        assertThatThrownBy(() -> new Price(price))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
