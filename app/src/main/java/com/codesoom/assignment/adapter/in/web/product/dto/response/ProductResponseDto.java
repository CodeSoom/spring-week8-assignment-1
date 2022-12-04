package com.codesoom.assignment.adapter.in.web.product.dto.response;

import com.codesoom.assignment.product.domain.port.command.ProductMapper;
import com.codesoom.assignment.product.repository.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductResponseDto {
    private Long id;
    private String name;
    private String maker;
    private Integer price;
    private String imageUrl;

    @Builder
    public ProductResponseDto(Long id, String name, String maker, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponseDto from(Product product) {
        return ProductMapper.INSTANCE.toResponse(product);
    }

    public static List<ProductResponseDto> fromList(List<Product> product) {
        return ProductMapper.INSTANCE.toResponseList(product);
    }
}
