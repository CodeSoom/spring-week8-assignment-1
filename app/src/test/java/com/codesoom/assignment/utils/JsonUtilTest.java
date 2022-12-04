package com.codesoom.assignment.utils;

import com.codesoom.assignment.common.utils.JsonUtil;
import com.codesoom.assignment.product.repository.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Jackson을 사용한 JsonUtil")
class JsonUtilTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class Product_도메인_엔티티는 {

        @Test
        @DisplayName("데이터를 직렬화한 후 역 직렬화하면 기존 데이터를 리턴한다")
        void write_read() throws IOException {

            Product originData = Product.builder()
                    .name("gibeom")
                    .price(Integer.MAX_VALUE)
                    .build();

            String requestStr = JsonUtil.writeValueAsString(originData);

            Product resultData = JsonUtil.readValue(requestStr, Product.class);

            assertThat(originData).isEqualTo(resultData);
        }
    }
}
