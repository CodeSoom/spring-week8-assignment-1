package com.codesoom.assignment.adapter.in.web.product.dto.response;

import com.codesoom.assignment.adapter.in.web.product.dto.ProductMapper;
import com.codesoom.assignment.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductResponseDto {
    private final Long id;
    private final String name;
    private final String maker;
    private final Integer price;
    private final String imageUrl;

    @Builder
    public ProductResponseDto(final Long id, final String name, final String maker,
                              final Integer price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponseDto from(final Product product) {
        return ProductMapper.INSTANCE.toResponse(product);
    }

    public static List<ProductResponseDto> fromList(final List<Product> product) {
        return ProductMapper.INSTANCE.toResponseList(product);
    }
}
